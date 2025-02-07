package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.entities.tecnicos.TrabajoPendiente;
import ar.edu.utn.frba.dds.models.repositories.TecnicosRepository;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TecnicoController implements ICrudViewsHandler {
    private TecnicosRepository tecnicosRepository;

    // ! { } Revisar
    public void crearTecnico(Tecnico nuevoTecnico) {
        this.tecnicosRepository.guardar(nuevoTecnico);
    }

    // ! { } Revisar
    public List<Tecnico> buscarTodos() {
        return this.tecnicosRepository.buscarTodos();
    }

    // ! { } Revisar
    public void actualizar(Tecnico tecnico) {
        this.tecnicosRepository.actualizar(tecnico);
    }

    // ! { } Revisar
    public void eliminar(Tecnico tecnico) {
        this.tecnicosRepository.eliminar(tecnico);
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

    public void gestionarIncidente(Incidente incidente, Tecnico tecnico){
        Optional<Heladera> heladera = ServiceLocator.instanceOf(HeladeraController.class).buscarPorNombre(incidente.getHeladera().getNombre());
        tecnicosRepository.actualizar(tecnico);
    }

    public void asignarTrabajoPendiente(Heladera heladeraEncontrada, Incidente incidente, String descripcion){

        System.out.println("Se creo el incidente asociado a la heladera: " + heladeraEncontrada);
        Tecnico tecnicoCercano = heladeraEncontrada.buscarTecnicoCercano();
        incidente.setTecnicoAsignado(tecnicoCercano);
        Optional<Heladera> heladera = ServiceLocator.instanceOf(HeladeraController.class).buscarPorNombre(incidente.getHeladera().getNombre());
        tecnicosRepository.actualizar(tecnicoCercano);

        // guardamos el trabajo pendiente correspondiente al incidente
        TrabajoPendiente trabajoPendiente = new TrabajoPendiente(tecnicoCercano, heladera,incidente, descripcion);
        try {
            tecnicosRepository.guardarTrabajoPendiente(trabajoPendiente);
            System.out.println("Trabajo pendiente " + trabajoPendiente.getDescripcion() + " guardado");
        } catch (Exception e) {
            System.out.println("Error al guardar trabajo pendiente: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Trabajo pendiente guardado");
    }
}

