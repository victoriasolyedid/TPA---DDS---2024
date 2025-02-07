package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.repositories.FormularioCompletoRepository;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FormularioCompletoController {
    private FormularioCompletoRepository formularioCompletoRepository;

    public FormularioCompleto guardar(Context context) {
        FormularioCompleto form = new FormularioCompleto();

        context.formParamMap().forEach((key, value) -> {
            if (!value.isEmpty()) {
                form.completarCampo(key,value.get(0));
            }
        });

        formularioCompletoRepository.guardar(form);
        return form;
    }

    public void guardarCargaMasiva(FormularioCompleto form) {
        formularioCompletoRepository.guardar(form);
    }
}
