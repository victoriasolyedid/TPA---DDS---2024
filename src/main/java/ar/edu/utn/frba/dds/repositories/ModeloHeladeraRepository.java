package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ModeloHeladeraRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<ModeloHeladera> buscarTodos() {
        return entityManager().createQuery("from " + ModeloHeladera.class.getName(), ModeloHeladera.class).getResultList();
    }

    @Override
    public Optional<ModeloHeladera> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(ModeloHeladera.class, id));
    }

    public Optional<ModeloHeladera> buscarPorNombre(String nombre) {
        String query = "SELECT h FROM ModeloHeladera h WHERE h.nombre = :nombre";
        TypedQuery<ModeloHeladera> typedQuery = entityManager().createQuery(query, ModeloHeladera.class);
        typedQuery.setParameter("nombre", nombre);
        return typedQuery.getResultStream().findFirst();
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof ModeloHeladera) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof ModeloHeladera) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof ModeloHeladera) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
