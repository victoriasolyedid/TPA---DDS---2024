package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.tecnicos.Visita;
import ar.edu.utn.frba.dds.models.repositories.VisitasTecnicoRepository;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class VisitaTecnicoController implements ICrudViewsHandler {
    private VisitasTecnicoRepository visitasTecnicoRepository;

    // ! { } Revisar
    public void crearVisita(Visita nuevaVisita) {
        this.visitasTecnicoRepository.guardar(nuevaVisita);
    }

    // ! { } Revisar
    public List<Visita> buscarTodos() {
        return this.visitasTecnicoRepository.buscarTodos();
    }

    // ! { } Revisar
    public void actualizar(Visita visita) {
        this.visitasTecnicoRepository.actualizar(visita);
    }

    // ! { } Revisar
    public void eliminar(Visita visita) {
        this.visitasTecnicoRepository.eliminar(visita);
    }

    // ! { } Revisar
    @Override
    public void index(Context context) {

    }

    // ! { } Revisar
    @Override
    public void crear(Context context) {

    }

    // ! { } Revisar
    @Override
    public void guardar(Context context) {

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
