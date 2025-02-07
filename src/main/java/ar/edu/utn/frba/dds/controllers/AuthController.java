package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.middlewares.AuthMiddleware;
import ar.edu.utn.frba.dds.models.entities.incidentes.HeladeraBroker2;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class AuthController {

    public HeladeraBroker2 heladeraBroker;
    // * {X} Revisar
    public void index(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        String registrado = ctx.queryParam("registrado");
        if ("true".equals(registrado)) {
            model.put("registrado", true);
        }
        ctx.render("auth/login.hbs", model);
    }

    public void iniciarSesion(Context ctx) {
        String username = ctx.formParam("usuario");
        String password = ctx.formParam("contrasenia");

        String nombreUsuario = usuarioValido(username, password);
        // Si las credenciales son válidas, establece una sesión para el usuario
        if (nombreUsuario != null) {
            ctx.sessionAttribute("username", nombreUsuario);
            ctx.sessionAttribute("auth", true);
            AuthMiddleware.setearUsuario(ctx);
            // TODO: REVISAR SI ESTA BIEN RECUPERAR EL ROL ASI
//            if(ctx.sessionAttribute("role") == TipoRol.JURIDICA){
//            heladeraBroker.iniciarBroker(ctx);
//            }
           // heladeraBroker.iniciarBroker(ctx);
            System.out.println("Usuario ingresado: " + nombreUsuario);
            ctx.redirect("/");
        } else {
            String error = "Credenciales incorrectas";
            Map<String, Object> model = new HashMap<>();
            model.put("error", error);
            ctx.render("auth/login.hbs", model);
        }
    }

    private String usuarioValido(String username, String password) {
        try {
            Usuario usuario = ServiceLocator.instanceOf(UsuariosRepository.class).buscarPorNombre(username);
            if (usuario != null && usuario.getNombreUsuario().equals(username) && usuario.getContrasenia().equals(password)) {
                return usuario.getNombreUsuario();
            }
        } catch (Exception e) {
            System.err.println("Error al buscar el usuario: " + e.getMessage());
        }
        return null;
    }

    public void cerrarSesion(Context ctx) {
        ctx.sessionAttribute("auth", false);
        ctx.sessionAttribute("username", null);
        ctx.sessionAttribute("role", null);
        ctx.sessionAttribute("user", null);
        ctx.redirect("/");
    }

    public static void actualizarUsuarioActual(Context ctx, Usuario usuario) {
        ctx.sessionAttribute("user", usuario);
        ctx.sessionAttribute("role", usuario.getRol().name());
    }
}