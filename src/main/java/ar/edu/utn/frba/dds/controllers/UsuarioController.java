package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.formularios.Formulario;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.entities.validador.Validador;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.roles.TipoRol;
import ar.edu.utn.frba.dds.utils.CamposToHTML;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class UsuarioController implements ICrudViewsHandler {
    private UsuariosRepository usuariosRepository;

    // * {X} Revisar
    @Override
    public void index(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        Usuario usuarioBuscado = usuariosRepository.buscarPorNombre(usuario.getNombreUsuario());
        FormularioCompleto formulario = usuarioBuscado.getColaborador().getDatosColaborador();

        boolean actualizado = Boolean.parseBoolean(ctx.queryParam("actualizado"));
        boolean error = Boolean.parseBoolean(ctx.queryParam("error"));

        model.put("datosColab", formulario);
        model.put("actualizado", actualizado);
        model.put("error", error);

        ctx.render("usuario/profile.hbs", model);
    }

    // * {X} Revisar
    public void cambiarContraseniaView(Context ctx) {
        ctx.render("usuario/resetPassword.hbs", ctx.sessionAttributeMap());
    }

    // * {X} Revisar
    public void cambiarContrasenia(Context ctx) {
        String contraseniaActual = ctx.formParam("contraseniaActual");
        String contraseniaNueva = ctx.formParam("contraseniaNueva");
        String contraseniaNuevaReingreso = ctx.formParam("contraseniaNueva1");

        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        String contraseniaGuardada = usuario.getContrasenia();
        assert contraseniaNueva != null;

        try {
            if (!contraseniaGuardada.equals(contraseniaActual)) {
                throw new Exception("La contraseña ingresada es incorrecta");
            }
            if (!contraseniaNueva.equals(contraseniaNuevaReingreso)) {
                throw new Exception("Las contraseñas no coinciden");
            }
            if (!Validador.esContraseniaSegura(usuario, contraseniaNueva)) {
                throw new Exception("La contraseña ingresada no es segura");
            }
        } catch (Exception error) {
            model.put("constraseniaModificada", false);
            model.put("error", error.getMessage());
            ctx.render("usuario/resetPassword.hbs", model);
            return;
        }

        usuario.setContrasenia(contraseniaNueva);
        usuario.agregarAlHistorial(contraseniaActual);
        model.put("contraseniaModificada", true);

        usuariosRepository.actualizar(usuario);
        AuthController.actualizarUsuarioActual(ctx, usuario);

        ctx.render("usuario/resetPassword.hbs", model);
    }

    // * {X} Revisar
    public void crear(Context ctx) {
        Optional<Formulario> formHumana = ServiceLocator.instanceOf(FormularioController.class).buscarPorNombre("formHumana");
        Optional<Formulario> formJuridica = ServiceLocator.instanceOf(FormularioController.class).buscarPorNombre("formJuridica");

        if (formHumana.isEmpty() || formJuridica.isEmpty()) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.redirect("/500");
            return;
        }

        List<String> camposHumana = CamposToHTML.getHtml(formHumana.get());
        List<String> camposJuridica = CamposToHTML.getHtml(formJuridica.get());

        Map<String, Object> model = ctx.sessionAttributeMap();
        model.put("camposHumana", camposHumana);
        model.put("camposJuridica", camposJuridica);

        ctx.render("usuario/register.hbs", model);
    }

    // * {X} Revisar
    public void guardar(Context ctx) {
        String nombreUsuario = ctx.formParam("usuario");
        Optional<Usuario> usuarioExistente = usuariosRepository.buscarUsuarioPorNombre(nombreUsuario);

        if (usuarioExistente.isPresent()) {
            ctx.sessionAttribute("error", "El nombre de usuario ya existe. Por favor, elija otro.");
            ctx.redirect("/register");
            return;
        }

        String nuevaContrasenia = ctx.formParam("password");
        Validador.esContraseniaSeguraNuevoUsuario(nuevaContrasenia);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setContrasenia(nuevaContrasenia);
        nuevoUsuario.agregarAlHistorial(nuevaContrasenia);

        if (Objects.equals(ctx.formParam("tipo_usuario"), "humana")) {
            nuevoUsuario.setRol(TipoRol.HUMANA);
        } else if (Objects.equals(ctx.formParam("tipo_usuario"), "juridica")) {
            nuevoUsuario.setRol(TipoRol.JURIDICA);
        }

        Colaborador nuevoColaborador = ServiceLocator.instanceOf(ColaboradorController.class).guardar(ctx);
        nuevoUsuario.setColaborador(nuevoColaborador);

        System.out.println("El usuario final que se va a guardar: " + nuevoUsuario);

        usuariosRepository.guardar(nuevoUsuario);
        ctx.redirect("/login?registrado=true");
    }

    public void guardarCargaMasiva(Usuario usuario) {
        usuariosRepository.guardar(usuario);
    }

    // ! { } Revisar
    @Override
    public void actualizar(Context ctx) {
        Usuario usuario = ctx.sessionAttribute("user");

        String nuevaDireccion = ctx.formParam("direccion");
        String nuevoTelefono = ctx.formParam("telefono");

        Colaborador colaborador = usuario.getColaborador();
        FormularioCompleto formulario = colaborador.getDatosColaborador();

        boolean actualizado = false;

        if (nuevaDireccion != null && !nuevaDireccion.isEmpty()) {
            formulario.completarCampo("direccion", nuevaDireccion);
            actualizado = true;
        }

        if (nuevoTelefono != null && !nuevoTelefono.isEmpty()) {
            formulario.completarCampo("telefono", nuevoTelefono);
            actualizado = true;
        }

        if (actualizado) {
            ServiceLocator.instanceOf(ColaboradorRepository.class).actualizar(colaborador);
            usuario = usuariosRepository.buscarPorNombre(usuario.getNombreUsuario());
            AuthController.actualizarUsuarioActual(ctx, usuario);
        }

        ctx.redirect("/mis-datos?actualizado=" + actualizado);
    }

    // * {X} Revisar
    @Override
    public void eliminar(Context ctx) {
        Optional<Usuario> usuario = this.usuariosRepository.buscar(Long.parseLong(ctx.pathParam("id")));
        if (usuario.isEmpty()) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.redirect("/404");
            return;
        }
        this.usuariosRepository.eliminar(usuario.get());

        ctx.redirect("/");
    }
}