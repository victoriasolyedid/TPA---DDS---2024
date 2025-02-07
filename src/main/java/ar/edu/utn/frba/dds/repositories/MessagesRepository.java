package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.suscripciones.Mensaje;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;

public class MessagesRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Mensaje> buscarTodos() {
        return entityManager().createQuery("from " + Mensaje.class.getName(), Mensaje.class).getResultList();
    }

    @Override
    public Optional<Mensaje> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Mensaje.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Mensaje) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Mensaje) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Mensaje) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
