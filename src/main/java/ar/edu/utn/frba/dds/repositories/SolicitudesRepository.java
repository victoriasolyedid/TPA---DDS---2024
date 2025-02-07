package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;

public class SolicitudesRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Solicitud> buscarTodos() {
        return entityManager().createQuery("from " + Solicitud.class.getName(), Solicitud.class).getResultList();
    }

    @Override
    public Optional<Solicitud> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Solicitud.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Solicitud) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Solicitud) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Solicitud) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
