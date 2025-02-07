package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.formularios.Formulario;
import ar.edu.utn.frba.dds.models.repositories.FormulariosRepository;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.*;
import java.util.*;

@AllArgsConstructor
public class FormularioController implements ICrudViewsHandler {
    private FormulariosRepository formulariosRepository;

    // * {X} Revisar
    public List<Formulario> buscarTodos() {
        return this.formulariosRepository.buscarTodos();
    }

    // * {X} Revisar
    public Optional<Formulario> buscar(Context context) {
        Long id = Long.valueOf(context.pathParam("id"));
        return this.formulariosRepository.buscar(id);
    }

    // * {X} Revisar | No recibe contexto porque no se usa
    public Optional<Formulario> buscarPorNombre(String nombre) {
        return this.formulariosRepository.buscarPorNombre(nombre);
    }

    // * {X} Revisar
    @Override
    public void index(Context context) {

    }

    // ? {X} Revisar
    @Override
    public void crear(Context context) {

    }

    @Override
    // * {X} Revisar
    public void guardar(Context context) {
        Formulario formulario = new Formulario();
        String nombreFormulario = context.formParam("nombreFormulario");
        List<String> camposFormulario = Collections.singletonList(context.formParam("camposFormulario"));
        formulario.setNombre(nombreFormulario);
        formulario.setCampos(camposFormulario);

        this.formulariosRepository.guardar(formulario);
    }

    // * {X} Revisar
    @Override
    public void actualizar(Context context) {
        Optional<Formulario> formularioActual = this.formulariosRepository.buscar(Long.valueOf(context.pathParam("id")));
        if (formularioActual.isEmpty()) {
            context.status(HttpStatus.NOT_FOUND);
            context.redirect("/404");
            return;
        }
        Formulario formulario = formularioActual.get();

        String nombreFormulario = context.formParam("nombreFormulario");
        String camposFormularioParam = context.formParam("camposFormulario");
        List<String> camposFormulario = camposFormularioParam != null ? Collections.singletonList(camposFormularioParam) : null;

        if (nombreFormulario != null && !nombreFormulario.isEmpty()) {
            formulario.setNombre(nombreFormulario);
        }
        if (camposFormulario != null) {
            formulario.setCampos(camposFormulario);
        }

        this.formulariosRepository.actualizar(formulario);
    }

    // * {X} Revisar
    @Override
    public void eliminar(Context context) {

    }
}