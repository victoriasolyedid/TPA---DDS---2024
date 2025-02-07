package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.factories.ColaboracionFactory;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DistribucionDeVianda;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidentes.HeladeraBroker2;
import ar.edu.utn.frba.dds.models.entities.importador.Importador;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.*;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UploadedFile;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@AllArgsConstructor
public class ColaboracionController implements ICrudViewsHandler {
    private ColaboracionRepository colaboracionRepository;
    private HeladerasRepository heladerasRepository;
    private ModeloHeladeraRepository modeloHeladeraRepository;
    private ColaboradorRepository colaboradorRepository;

    private HeladeraBroker2 heladeraBroker2;
    // ! { } Revisar
    public List<TipoDeColaboracion> buscarTodos() {
        return this.colaboracionRepository.buscarTodos();
    }

    // ! { } Revisar
    public List<TipoDeColaboracion> buscarUltimaSemana() {
        return this.colaboracionRepository.buscarUltimaSemana();
    }

    // ! { } Revisar
    public void colaboracionValidada(TipoDeColaboracion tipoDeColaboracion, Colaborador colaborador) {
        colaborador.getColaboraciones().add(tipoDeColaboracion);
        this.colaboracionRepository.guardar(tipoDeColaboracion);
    }

    public void gestionarColaboracionesView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");
        if (user != null) {
            model.put("colaboraciones", user.getColaborador().getColaboraciones());
        }
        ctx.render("colaboraciones/gestionarColaboraciones.hbs", model);
    }

    public void cargarColaboracionesView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");

        ctx.render("colaboraciones/cargarColaboraciones.hbs", model);
    }

    public void cargarColaboraciones(Context ctx) {
        UploadedFile file = ctx.uploadedFile("file");
        if(file!=null) {
            Importador importador = new Importador();
            importador.leerArchivo(file.content());
        } else {
            System.out.println("El archivo que está intentando importarse está vacío.");
        }
        // Redirigir a algún lado donde puedan verse las colabs importadas
        // ctx.redirect("/colaboraciones-cargadas");
    }

    public void verColabsCargadas(Context ctx) {
        /*Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");
        if (user != null) {
            model.put("colaboraciones", user.getColaborador().getColaboraciones());
        }*/
        ctx.render("colaboraciones/verColaboracionesCargadas.hbs");
    }

    // * {X} Revisar
    @Override
    public void crear(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");
        if (user != null) {
            Colaborador colaborador = user.getColaborador();
            FormularioCompleto formulario = colaborador.getDatosColaborador();
            model.put("colaborador", colaborador);
            model.put("datosColab", formulario);
        }
        List<ModeloHeladera> modelosHeladera = modeloHeladeraRepository.buscarTodos();
        model.put("modelosHeladera", modelosHeladera);
        ctx.render("colaboraciones/nuevaColaboracion.hbs", model);
    }

    // ? {X} Revisar | Falta testear
    @Override
    public void guardar(Context ctx) {
        TipoDeColaboracion colaboracion = ColaboracionFactory.crear(ctx, heladerasRepository);

        if (colaboracion instanceof HacerseCargoDeHeladera ){
            heladeraBroker2.iniciarBroker(ctx);
        }
        if (colaboracion instanceof DonacionDeViandas || colaboracion instanceof DistribucionDeVianda){
            Optional<Colaborador> colaborador = colaboracionRepository.buscarColaboradorPorColaboracionId(colaboracion.getId());
            Optional<Heladera> heladera = colaboracionRepository.buscarHeladeraPorColaboracionId(colaboracion.getId());

            if(colaborador.isPresent() && heladera.isPresent()){
                colaborador.get().agregarZona(heladera.get().getUbicacion().getLocalidad());
                colaboradorRepository.actualizar(colaborador);
                ServiceLocator.instanceOf(HeladeraController.class).verificarPermisosParaAbrir(colaboracion,colaborador.get());
            }

        }
        this.colaboracionRepository.guardar(colaboracion);
        Usuario usuario = ServiceLocator.instanceOf(UsuariosRepository.class).buscarPorUsername(ctx.sessionAttribute("username")).get();
        AuthController.actualizarUsuarioActual(ctx, usuario);
        ctx.redirect("/mis-colaboraciones");
    }

    public void guardarCargaMasiva(TipoDeColaboracion colab) {
        colaboracionRepository.guardar(colab);
    }

    // ? {X} Revisar | Redirección pendiente
    @Override
    public void eliminar(Context ctx) {
        try {
            Long idColaboracion = Long.parseLong(ctx.pathParam("id"));
            Optional<TipoDeColaboracion> colaboracion = this.colaboracionRepository.buscar(idColaboracion);

            if (colaboracion.isEmpty()) {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.redirect("/404");
                return;
            }

            this.colaboracionRepository.eliminar(colaboracion.get());
            ctx.redirect("/gestionar-colaboraciones?eliminada=1"); // TODO: Ver cómo hacer esto
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.redirect("/gestionar-colaboraciones?error=1"); // TODO: Ver cómo hacer esto
        }
    }

    // ? {X} Revisar | Creemos que no se usa
    @Override
    public void actualizar(Context ctx) {

    }

    // ? No se usa
    @Override
    public void index(Context ctx) {

    }
}


