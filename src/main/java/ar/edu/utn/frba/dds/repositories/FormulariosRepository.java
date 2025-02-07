package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.formularios.Formulario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;

public class FormulariosRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Formulario> buscarTodos() {
        return entityManager().createQuery("from " + Formulario.class.getName(), Formulario.class).getResultList();
    }

    @Override
    public Optional<Formulario> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Formulario.class, id));
    }

    public Optional<Formulario> buscarPorNombre(String nombre) {
        return entityManager()
                .createQuery("SELECT f FROM Formulario f WHERE f.nombre = :nombre", Formulario.class)
                .setParameter("nombre", nombre)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Formulario) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Formulario) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Formulario) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
