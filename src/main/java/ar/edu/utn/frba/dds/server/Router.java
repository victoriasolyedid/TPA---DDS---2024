package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.*;
import ar.edu.utn.frba.dds.middlewares.AuthMiddleware;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoIncidente;
import ar.edu.utn.frba.dds.roles.TipoRol;
import io.javalin.Javalin;

import java.io.File;
import java.io.FileInputStream;

public class Router {
    public static void init(Javalin app) {
        app.beforeMatched(AuthMiddleware::manejarAutenticacion);

        app.error(404, ctx -> {
            ctx.render("404.hbs", ctx.sessionAttributeMap());
        });

        app.get("/", ctx -> {
            ctx.render("index.hbs", ctx.sessionAttributeMap());
        });

        // --- COLABORACIONES ---
        app.get("/nueva-colaboracion", ServiceLocator.instanceOf(ColaboracionController.class)::crear, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.post("/nueva-colaboracion", ServiceLocator.instanceOf(ColaboracionController.class)::guardar, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.get("/mis-colaboraciones", ServiceLocator.instanceOf(ColaboracionController.class)::gestionarColaboracionesView, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.get("/carga-masiva", ServiceLocator.instanceOf(ColaboracionController.class)::cargarColaboracionesView, TipoRol.ADMIN);
        app.post("/carga-masiva", ServiceLocator.instanceOf(ColaboracionController.class)::cargarColaboraciones, TipoRol.ADMIN);
        app.get("/colaboraciones-cargadas", ServiceLocator.instanceOf(ColaboracionController.class)::verColabsCargadas, TipoRol.ADMIN);

        // --- ALERTAS / INCIDENTES ---
        app.get("/alertas", ServiceLocator.instanceOf(IncidenteController.class)::alertasView, TipoRol.JURIDICA);
        app.get("/reportar-falla", ServiceLocator.instanceOf(IncidenteController.class)::index, TipoRol.INVITADO);
        app.post("/reportar-falla", ctx -> {
            ServiceLocator.instanceOf(IncidenteController.class).crear(ctx, TipoIncidente.FALLA_TECNICA);
        }, TipoRol.INVITADO);

        app.get("/incidentes-asignados", ServiceLocator.instanceOf(IncidenteController.class)::trabajosPendientesView, TipoRol.TECNICO);

        app.get("/gestionar-incidente", ServiceLocator.instanceOf(IncidenteController.class)::gestionarIncidenteView, TipoRol.TECNICO);
        app.post("/gestionar-incidente", ctx -> {
            ServiceLocator.instanceOf(IncidenteController.class).gestionarIncidente(ctx);
        }, TipoRol.TECNICO);

        // --- HELADERAS ---
        app.get("/heladeras", ServiceLocator.instanceOf(HeladeraController.class)::index, TipoRol.INVITADO);
        app.get("/nueva-heladera", ServiceLocator.instanceOf(HeladeraController.class)::nuevaHeladeraView, TipoRol.JURIDICA);
        app.post("/nueva-heladera", ServiceLocator.instanceOf(HeladeraController.class)::crear, TipoRol.JURIDICA);
        app.get("/modificar-heladera/{id}", ServiceLocator.instanceOf(HeladeraController.class)::modificarHeladeraView, TipoRol.JURIDICA);
        app.post("/modificar-heladera/{id}", ServiceLocator.instanceOf(HeladeraController.class)::actualizar, TipoRol.JURIDICA);

        // --- APERTURAS ---
        app.get("/nueva-apertura", ServiceLocator.instanceOf(SolicitudController.class)::index, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.post("/nueva-apertura", ServiceLocator.instanceOf(SolicitudController.class)::guardar, TipoRol.JURIDICA, TipoRol.HUMANA);

        // --- CATALOGO ---
        app.get("/catalogo", ServiceLocator.instanceOf(ProductoController.class)::index, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.get("/prod-serv", ServiceLocator.instanceOf(ProductoController.class)::publicarView, TipoRol.JURIDICA);
        app.post("/prod-serv", ServiceLocator.instanceOf(ProductoController.class)::crear, TipoRol.JURIDICA);
        app.post("/canjear-producto", ServiceLocator.instanceOf(ProductoController.class)::canjearProducto, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.get("/mis-prod-serv", ServiceLocator.instanceOf(ProductoController.class)::misProdServView, TipoRol.JURIDICA, TipoRol.HUMANA);

        // --- REPORTES ---
        app.get("/reportes", ServiceLocator.instanceOf(ReportesController.class)::reporteView, TipoRol.ADMIN);
        app.get("/reportes/pdfs/{filename}", ctx -> {
            String filename = ctx.pathParam("filename");
            File file = new File("src/main/java/ar/edu/utn/frba/dds/reportes/pdfs/" + filename);
            if (file.exists()) {
                ctx.contentType("application/pdf");
                ctx.result(new FileInputStream(file));
            } else {
                ctx.status(404).result("File not found");
            }
        });

        // --- SUSCRIPCIONES ----
        app.get("/nueva-suscripcion", ServiceLocator.instanceOf(SuscripcionController.class)::index, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.post("/nueva-suscripcion", ServiceLocator.instanceOf(SuscripcionController.class)::registrarSuscripcion, TipoRol.JURIDICA, TipoRol.HUMANA);

        // --- AUTH ---
        app.get("/login", ServiceLocator.instanceOf(AuthController.class)::index, TipoRol.INVITADO);
        app.post("/login", ServiceLocator.instanceOf(AuthController.class)::iniciarSesion, TipoRol.INVITADO);
        app.get("/logout", ServiceLocator.instanceOf(AuthController.class)::cerrarSesion, TipoRol.JURIDICA, TipoRol.HUMANA);

        // --- USUARIO ---
        app.get("/mis-datos", ServiceLocator.instanceOf(UsuarioController.class)::index, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.post("/mis-datos", ServiceLocator.instanceOf(UsuarioController.class)::actualizar, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.get("/nueva-contrasenia", ServiceLocator.instanceOf(UsuarioController.class)::cambiarContraseniaView, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.post("/nueva-contrasenia", ServiceLocator.instanceOf(UsuarioController.class)::cambiarContrasenia, TipoRol.JURIDICA, TipoRol.HUMANA);
        app.get("/register", ServiceLocator.instanceOf(UsuarioController.class)::crear, TipoRol.INVITADO);
        app.post("/register", ServiceLocator.instanceOf(UsuarioController.class)::guardar, TipoRol.INVITADO);
        app.post("/eliminar-usuario/{id}", ServiceLocator.instanceOf(UsuarioController.class)::eliminar, TipoRol.ADMIN);

//        //BROKER
//        app.get("/iniciar-broker-apertura", ctx -> {
//            // Llamada a la función que inicia el broker
//            ServiceLocator.instanceOf(ColaboradorController.class)::iniciarBroker, TipoRol.HUMANA, TipoRol.JURIDICA);
//        });
    }
}
