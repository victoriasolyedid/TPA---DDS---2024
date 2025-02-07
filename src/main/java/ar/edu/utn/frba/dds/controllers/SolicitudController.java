package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.factories.SolicitudFactory;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Permiso;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.AperturasRepository;
import ar.edu.utn.frba.dds.models.repositories.ColaboracionRepository;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.SolicitudesRepository;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class SolicitudController implements ICrudViewsHandler {
    private SolicitudesRepository solicitudesRepository;
    private HeladerasRepository heladerasRepository;
    private ColaboracionRepository colaboracionesRepository;
    private AperturasRepository aperturasRepository;

    // ! { } Revisar
    public void guardar(Solicitud solicitud) {
        this.solicitudesRepository.guardar(solicitud);
    }

    // ! { } Revisar
    public Optional<Solicitud> buscar(Long id) {
        return (Optional<Solicitud>) this.solicitudesRepository.buscar(id);
    }

    // ! { } Revisar
    public List<Solicitud> buscarTodos() {
        return this.solicitudesRepository.buscarTodos();
    }

    // ! { } Revisar
    public void eliminar(Solicitud solicitud) {
        this.solicitudesRepository.eliminar(solicitud);
    }

    // ! { } Revisar
    public void agregarPermiso(Colaborador colaborador, Heladera heladera, LocalDateTime hora) {
        var nuevoPermiso = new Permiso(heladera, hora);
        var permisosDelColab = colaborador.getTarjeta().getPermisos();
        permisosDelColab.add(nuevoPermiso);
    }

    @Override
    public void index(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        if (usuario != null) {
            List<Heladera> heladeras = this.heladerasRepository.buscarHeladerasPorColaborador(usuario.getColaborador().getId());
            model.put("heladeras", heladeras);
            ctx.render("aperturas/nuevaApertura.hbs", model);

        }
    }

    // ! { } Revisar
    @Override
    public void crear(Context context) {

    }

    // ! { } Revisar
    @Override
    public void guardar(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        String idHeladera = ctx.formParam("heladera");
        Optional<Heladera> heladeraOpt = ServiceLocator.instanceOf(HeladerasRepository.class).buscar(Long.valueOf(idHeladera));
        if (heladeraOpt.isPresent()) {
            Solicitud solicitud = SolicitudFactory.crear(ctx,heladeraOpt);
            this.solicitudesRepository.guardar(solicitud);
        } else {
            throw new IllegalArgumentException("Heladera no encontrada: " + heladeraOpt);
        }
        ctx.redirect("/nueva-apertura");


    }

    // ! { } Revisar
    @Override
    public void actualizar(Context context) {


    }

    // ! { } Revisar
    @Override
    public void eliminar(Context context) {

    }
}
