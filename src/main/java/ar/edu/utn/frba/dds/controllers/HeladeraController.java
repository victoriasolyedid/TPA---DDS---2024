package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.factories.HeladeraFactory;
import ar.edu.utn.frba.dds.factories.IncidenteFactory;
import ar.edu.utn.frba.dds.middlewares.AuthMiddleware;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.Mail;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Apertura;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.incidentes.Alerta;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoAlerta;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.tarjetas.GestorDePermisos;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.*;
import ar.edu.utn.frba.dds.roles.TipoRol;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AllArgsConstructor;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HeladeraController implements ICrudViewsHandler {
    private HeladerasRepository heladerasRepository;
    private SuscripcionRepository suscripcionRepository;
    private SolicitudController solicitudController;
    private ColaboracionController colaboracionController;
    private SuscripcionController suscripcionController;
    private TecnicoController tecnicoController;

    private Mail mailNotificador;
    public List<Heladera> buscarTodos() {
        return this.heladerasRepository.buscarTodos();
    }

    public Optional<Heladera> buscar(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        return this.heladerasRepository.buscar(id);
    }

    public Optional<Heladera> buscarPorNombre(String nombre) {
        return this.heladerasRepository.buscarPorNombre(nombre);
    }

    // ! { } Revisar
    public void verificarPermisosParaAbrir(TipoDeColaboracion colaboracion, Colaborador colaborador) {
        var permisosSobreLaHeladeraStream = GestorDePermisos.buscarPermisos(colaborador, colaboracion);
        var permisosSobreLaHeladera = permisosSobreLaHeladeraStream.toList();

        var permisoValidado = GestorDePermisos.validarPermisos(permisosSobreLaHeladera.stream());

        List<Solicitud> todasLasSolicitudes = solicitudController.buscarTodos();

        // Filtrar las solicitudes para encontrar la que coincida con ambos IDs
        Optional<Solicitud> solicitud = todasLasSolicitudes.stream()
                .filter(s -> s.getColaboracion().getId().equals(colaboracion.getId()) &&
                        s.getColaborador().getId().equals(colaborador.getId()))
                .findFirst();

        if (solicitud.isPresent() && permisoValidado.isPresent()) {
            colaboracionController.colaboracionValidada(colaboracion, colaborador);
            GestorDePermisos.revocarPermisos(colaborador, permisoValidado.get());
            GestorDePermisos.agregarUso(colaborador, colaboracion);
            registrarApertura(colaboracion);
        } else {
            permisosSobreLaHeladera.forEach(permiso -> GestorDePermisos.revocarPermisos(colaborador, permiso));
            System.out.println("El colaborador no realizó la solicitud de apertura o caducó su tiempo de solicitud, no se le agregó la colaboración");
        }
    }

    // ! { } Revisar
    public void registrarApertura(TipoDeColaboracion colaboracion) {
        Apertura apertura = new Apertura(colaboracion.getHeladera());
        // aperturaController.guardar(apertura); FIXME
        System.out.println("Se registró la apertura de la heladera  " + apertura);
    }

    public void nuevaHeladeraView(Context ctx) {
        ctx.render("heladeras/nuevaHeladera.hbs", ctx.sessionAttributeMap());
    }

    @Override
    public void index(Context ctx) {
        List<Heladera> heladeras = this.heladerasRepository.buscarTodos();

        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");
        String userRole = AuthMiddleware.getUserRole(ctx);

        if (TipoRol.JURIDICA.name().equals(userRole) || TipoRol.HUMANA.name().equals(userRole)) {
            List<Heladera> suscripcionHeladeras = this.suscripcionRepository.buscarSuscripcionesPorUsuario(usuario.getColaborador().getId())
                    .stream()
                    .map(Suscripcion::getHeladera)
                    .toList();

            model.put("misSuscripciones", suscripcionHeladeras);
        }

        if (TipoRol.JURIDICA.name().equals(userRole)) {
            List<Heladera> misHeladeras = this.heladerasRepository.buscarHeladerasPorColaborador(usuario.getColaborador().getId());
            model.put("misHeladeras", misHeladeras);
        }

        model.put("heladeras", heladeras);
        ctx.render("heladeras/heladeras.hbs", model);
    }

    // * {X} Revisar
    public void modificarHeladeraView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        if (usuario != null) {
            Long idHeladera = Long.valueOf(ctx.pathParam("id"));
            Heladera heladera = this.heladerasRepository.buscar(idHeladera).orElse(null);
            model.put("heladera", heladera);

            List<Heladera> heladeras = this.heladerasRepository.buscarHeladerasPorColaborador(usuario.getColaborador().getId());
            model.put("heladeras", heladeras);
            ctx.render("heladeras/modificarHeladera.hbs", model);
        }
    }

    @Override
    // ? {X} Revisar
    public void crear(Context ctx) throws IOException {
        Heladera heladera = HeladeraFactory.crear(ctx);
        this.heladerasRepository.guardar(heladera);
        suscripcionController.handleMessage(heladera);
        ctx.render("heladeras/nuevaHeladera.hbs");
    }

    @Override
    // * {X} Revisar
    public void guardar(Context ctx) throws IOException {
        Heladera heladera = HeladeraFactory.crear(ctx);
        suscripcionController.handleMessage(heladera);
    }

    @Override
    // * {X} Revisar
    public void actualizar(Context ctx) {
       /* Optional<Heladera> heladeraActual = this.heladerasRepository.buscar(Long.valueOf(ctx.pathParam("id")));
        if (heladeraActual.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.redirect("/404");
            return;
        }
        Heladera heladera = heladeraActual.get();

        // Traer los datos del formulario y asignar los valores a la instancia heladera (debemos mantener la capacidad y la fecha de funcionamiento)
        asignarParametros(heladera, ctx);

        this.heladerasRepository.actualizar(heladera);

        // Preparamos el modelo para la vista
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");
        if (usuario != null) {
            model.put("heladera", heladera);
            model.put("actualizar", true);

            ctx.render("heladeras/modificarHeladera.hbs", model);
        }*/
    }

    @Override
    // * {X} Revisar
    public void eliminar(Context ctx) {
        Optional<Heladera> heladera = this.heladerasRepository.buscar(Long.parseLong(ctx.pathParam("id")));
        if (heladera.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.redirect("/404");
            return;
        }
        this.heladerasRepository.eliminar(heladera.get());
        ctx.redirect("/heladeras?eliminada=1");
    }

    public void temperaturaFueraDeRango(Heladera heladera) throws MessagingException {
        heladerasRepository.actualizar(heladera);

        var temperaturaMaxima = heladera.getModelo().getTemperaturaMaxima();
        var temperaturaMinima = heladera.getModelo().getTemperaturaMinima();

        if (heladera.getTemperaturaActual() > temperaturaMaxima || heladera.getTemperaturaActual() < temperaturaMinima) {
            this.desactivarHeladera(heladera, TipoAlerta.TEMPERATURA);
            System.out.println("La heladera " + heladera + " fue desactivada.");
        }
    }

    public void desactivarHeladera(Heladera heladera, TipoAlerta tipoAlerta) throws MessagingException {
        if (heladera.getActiva()) {
            System.out.println("La heladera " + heladera.getNombre() + " fue desactivada.");
            heladera.setActiva(false);
            heladerasRepository.actualizar(heladera);
        } else {
            System.out.println("La heladera " + heladera.getNombre() + " ya fue desactivada previamente.");
        }

        Alerta alertaTemperaturaFueraDeRango = new Alerta(heladera, tipoAlerta);
        var alertaFinal = IncidenteFactory.crearAlerta(alertaTemperaturaFueraDeRango);
        tecnicoController.asignarTrabajoPendiente(heladera, alertaFinal, "Se reporto la alerta: "  + tipoAlerta);

        Tecnico tecnicoCercano = heladera.buscarTecnicoCercano();

        if (tecnicoCercano == null) {
            System.out.println("No se encontró un técnico cercano.");
            return;
        } else {
            System.out.println("Técnico cercano encontrado: " + tecnicoCercano.getNombre());
            mailNotificador.enviarMensajeA(tecnicoCercano, "Se reportó una ALERTA", tecnicoCercano.getMail());
            System.out.println("Mensaje enviado exitosamente al técnico: " + tecnicoCercano);

        }
    }
}

