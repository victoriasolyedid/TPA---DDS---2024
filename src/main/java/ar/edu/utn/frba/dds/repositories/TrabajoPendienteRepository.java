package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.tecnicos.TrabajoPendiente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class TrabajoPendienteRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List buscarTodos() {
        return List.of();
    }

    @Override
    public TrabajoPendiente buscar(Long id) {
        return entityManager().find(TrabajoPendiente.class, id);
    }

    @Override
    public void guardar(Object o) {

    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof TrabajoPendiente) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        entityManager().remove(entityManager().contains(o) ? o : entityManager().merge(o));
    }
}
