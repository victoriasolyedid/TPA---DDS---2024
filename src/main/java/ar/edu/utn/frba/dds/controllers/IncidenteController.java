package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.factories.IncidenteFactory;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.entities.tecnicos.TrabajoPendiente;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.*;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class IncidenteController implements ICrudViewsHandler {
    private IncidentesRepository incidentesRepository;
    private HeladerasRepository heladerasRepository;
    private ColaboracionRepository colaboracionRepository;
    private TecnicoController tecnicoController;
    private TecnicosRepository tecnicosRepository;
    private TrabajoPendienteRepository trabajoPendienteRepository;

    public List<Incidente> buscarTodos() {
        return this.incidentesRepository.buscarTodos();
    }

    public List<Incidente> buscarUltimaSemana() {
        return this.incidentesRepository.buscarUltimaSemana();
    }

    public void actualizar(Incidente incidente) {
        this.incidentesRepository.actualizar(incidente);
    }

    public void eliminar(Incidente incidente) {
        this.incidentesRepository.eliminar(incidente);
    }

    public void index(Context ctx) {
        List<Heladera> heladeras = heladerasRepository.buscarTodos();
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");
        String incidenteCreado = ctx.queryParam("incidenteCreado");
        if ("true".equals(incidenteCreado)) {
            model.put("incidenteCreado", true);
        }

        if (usuario != null) {
            model.put("heladeras", heladeras);
            ctx.render("incidentes/reportarFallas.hbs", model);
        }
    }

    @Override
    public void crear(Context ctx) {

    }

    @Override
    public void guardar(Context ctx) {

    }

    @Override
    public void actualizar(Context ctx) {

    }

    @Override
    public void eliminar(Context ctx) {

    }

    public void crear(Context ctx, TipoIncidente tipoIncidente) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        Optional<Heladera> heladeraEncontrada = heladerasRepository.buscar(Long.valueOf(Objects.requireNonNull(ctx.formParam("heladera"))));
        // Poner la heladera como no disponible, ACTIVA = false
        heladeraEncontrada.ifPresent(heladera -> heladera.setActiva(false));

        Incidente incidente = IncidenteFactory.crear(ctx, tipoIncidente);

        if (heladeraEncontrada.isPresent()) {
            System.out.println("Creamos el incidente asociado a la heladera: " + heladeraEncontrada);
            Tecnico tecnicoCercano = heladeraEncontrada.get().buscarTecnicoCercano();
            incidente.setTecnicoAsignado(tecnicoCercano);
            tecnicoController.gestionarIncidente(incidente,tecnicoCercano);

            // guardamos el trabajo pendiente correspondiente al incidente creado
            TrabajoPendiente trabajoPendiente = new TrabajoPendiente(tecnicoCercano,heladeraEncontrada,incidente, ctx.formParam("descripcion"));
            tecnicosRepository.guardarTrabajoPendiente(trabajoPendiente);
        }

        System.out.println("A continuación se guarda el incidente: " + incidente);
        this.incidentesRepository.guardar(incidente);

        ctx.redirect("/reportar-falla?incidenteCreado=true");
    }

    public void alertasView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuarioActual = (Usuario) model.get("user");

        List<HacerseCargoDeHeladera> colaboraciones = colaboracionRepository.buscarColaboracionesPorTipo(
                usuarioActual.getColaborador().getId(),
                HacerseCargoDeHeladera.class
        );

        List<Incidente> incidentes = new ArrayList<>();
        Map<Long, String> nombresHeladerasMap = new HashMap<>(); // Mapa para asociar heladeras con nombres

        for (HacerseCargoDeHeladera colaboracion : colaboraciones) {
            Heladera heladera = colaboracion.getHeladera();
            if (heladera != null) {
                nombresHeladerasMap.put(heladera.getId(), heladera.getNombre());
                List<Incidente> incidentesHeladera = incidentesRepository.buscarPorHeladera(heladera.getId());
                incidentes.addAll(incidentesHeladera);
            }
        }

        model.put("nombresHeladeras", nombresHeladerasMap);
        model.put("alertas", incidentes);

        ctx.render("incidentes/gestionarAlertas.hbs", model);
    }

    // opcion de INCIDENTES ASIGNADOS
    public void trabajosPendientesView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");

        Optional<Tecnico> tecnico = tecnicosRepository.buscar(user.getTecnico().getId());

        if (tecnico.isPresent()) {
            List<TrabajoPendiente> trabajosPendientes = tecnico.get().getTrabajosPendientes();
            List<TrabajoPendiente> trabajosPendientesNoResueltos = trabajosPendientes.stream()
                    .filter(trabajo -> Boolean.FALSE.equals(trabajo.getResuelto()))
                    .collect(Collectors.toList());

            if (user != null) {
                model.put("trabajosPendientes", trabajosPendientesNoResueltos);
            }
        }
        System.out.println("Mostramos la lista de trabajos pendientes");
        ctx.render("incidentes/trabajosPendientes.hbs", model);
    }

    // opcion de GESTIONAR INCIDENTE
    public void gestionarIncidenteView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");

        Optional<Tecnico> tecnico = tecnicosRepository.buscar(user.getTecnico().getId());
        if (tecnico.isPresent()) {
            List<TrabajoPendiente> trabajosPendientes = tecnico.get().getTrabajosPendientes();
            List<TrabajoPendiente> trabajosPendientesNoResueltos = trabajosPendientes.stream()
                    .filter(trabajo -> Boolean.FALSE.equals(trabajo.getResuelto()))
                    .collect(Collectors.toList());

            model.put("trabajosPendientes", trabajosPendientesNoResueltos);
            String incidenteResuelto = ctx.queryParam("incidenteResuelto");
            if ("true".equals(incidenteResuelto)) {
                model.put("incidenteResuelto", true);
            }
            ctx.render("incidentes/incidentes.hbs", model);
        }
    }

    // elegir incidente para marcarlo como resuelto
    public void gestionarIncidente(Context ctx) {
        // obtengo el ID del trabajo pendiente elegido = id del incidente
        System.out.println("OPCION ELEGIDA: " + ctx.formParam("trabajoRealizado"));
        Long idTrabajo = Long.valueOf(ctx.formParam("trabajoRealizado"));
        TrabajoPendiente trabajoPendiente = trabajoPendienteRepository.buscar(idTrabajo);
        System.out.println("TRABAJO PENDIENTE ELEGIDO: " + trabajoPendiente);

        // Tengo que modificar el incidente en la base de datos para que figure como resuelto
        Incidente incidente = trabajoPendiente.getIncidente();
        System.out.println("INCIDENTE ASOCIADO: " + incidente);

        // al incidente que tengo, le tengo que cambiar el atributo de resuelto y actualizarlo en la db
        incidente.setResuelto(true);
        System.out.println("INCIDENTE ACTUALIZADO: " + incidente);
        incidentesRepository.actualizar(incidente);

        trabajoPendiente.setResuelto(true);
        trabajoPendienteRepository.actualizar(trabajoPendiente);

        // Poner la heladera como disponible, ACTIVA = true
        Heladera heladera = incidente.getHeladera();
        heladera.setActiva(true);
        heladerasRepository.actualizar(heladera);

        TrabajoPendiente trabajoPendienteEliminar = trabajoPendienteRepository.buscar(idTrabajo);
        // eliminar de la tabla de trabajos pendientes el que se acaba de resolver
        System.out.println("ELIMINANDO TRABAJO PENDIENTE: " + trabajoPendienteEliminar);

        ctx.redirect("/gestionar-incidente?incidenteResuelto=true");
    }
}
