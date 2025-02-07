package ar.edu.utn.frba.dds.middlewares;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.AuthController;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.roles.TipoRol;
import io.javalin.http.Context;

public class AuthMiddleware {
    public static void setearUsuario(Context ctx) {
        if (esArchivoEstatico(ctx)) return;

        Boolean isAuthenticated = ctx.sessionAttribute("auth");
        if (isAuthenticated != null && isAuthenticated) {
            String user = ctx.sessionAttribute("username");
            Usuario usuario = ServiceLocator.instanceOf(UsuariosRepository.class).buscarPorNombre(user);
            AuthController.actualizarUsuarioActual(ctx, usuario);
            ctx.attribute("userData", usuario);

            // Verificar si el usuario tiene el rol de TECNICO y agregar el nombre del técnico al contexto
            if (TipoRol.TECNICO.name().equals(usuario.getRol().name())) {
                ctx.sessionAttribute("nombreTecnico", usuario.getTecnico().getNombre());
            }
        }

        System.out.println("---- Usuario: " + ctx.attribute("userData"));
        System.out.println("Usuario autenticado: " + ctx.sessionAttributeMap());
    }

    public static boolean requerirAutenticacion(Context ctx) {
        if (esArchivoEstatico(ctx)) return false;

        Boolean isAuthenticated = ctx.sessionAttribute("auth");
        if (isAuthenticated == null || !isAuthenticated) {
            ctx.redirect("/login");
            return true;
        }
        return false;
    }

    public static void redirigirSiEstaAutenticado(Context ctx) {
        if (esArchivoEstatico(ctx)) return;

        Boolean isAuthenticated = ctx.sessionAttribute("auth");
        if (isAuthenticated != null && isAuthenticated) {
            ctx.redirect("/");
        }
    }

    private static boolean esArchivoEstatico(Context ctx) {
        String path = ctx.path();
        return path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images") || path.startsWith("/uploads");
    }

    public static void manejarAutenticacion(Context ctx) {
        var rolesPermitidos = ctx.routeRoles().stream()
                .filter(TipoRol.class::isInstance)
                .map(TipoRol.class::cast)
                .toList();
        if (rolesPermitidos.isEmpty()) return;
        String role = getUserRole(ctx);

        if ("/login".equals(ctx.path()) || "/register".equals(ctx.path())) {
            redirigirSiEstaAutenticado(ctx);
            return;
        }

        if (!"/".equals(ctx.path()) && !"/heladeras".equals(ctx.path())) {
            boolean redirigido = requerirAutenticacion(ctx);
            if (redirigido) return;
        }

        if (!role.equals(TipoRol.ADMIN.name()) && !rolesPermitidos.contains(TipoRol.INVITADO) && rolesPermitidos.stream().map(TipoRol::name).noneMatch(role::equals)) {
            ctx.redirect("/");
        }
    }

    public static String getUserRole(Context ctx) {
        String role = ctx.sessionAttribute("role");
        return role != null ? role : TipoRol.INVITADO.name();
    }
}