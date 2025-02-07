package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.entities.tecnicos.TrabajoPendiente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TecnicosRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Tecnico> buscarTodos() {
        return entityManager().createQuery("from " + Tecnico.class.getName(), Tecnico.class).getResultList();
    }

    @Override
    public Optional<Tecnico> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Tecnico.class, id));
    }

    // Método en TecnicosRepository para guardar un trabajo pendiente
    public void guardarTrabajoPendiente(TrabajoPendiente trabajoPendiente) {
        System.out.println("Guardandoo");
        entityManager().persist(trabajoPendiente);
        System.out.println("Se guarda el trabajo pendiente");
    }

    public List<TrabajoPendiente> obtenerTrabajosPendientesPorTecnico(Long idTecnico) {
        try {
            return entityManager()
                    .createQuery(
                            "SELECT t FROM TrabajoPendiente t " +
                                    "WHERE t.tecnico.id = :idTecnico",
                            TrabajoPendiente.class
                    )
                    .setParameter("idTecnico", idTecnico)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList(); // Devuelve una lista vacía si no hay resultados.
        }
    }


    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Tecnico) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Tecnico) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Tecnico) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }

    public Optional<TrabajoPendiente> buscarTrabajoPendientePorIdTrabajoPendiente(Long trabajoPendienteId) {
        return Optional.ofNullable(entityManager().find(TrabajoPendiente.class, trabajoPendienteId));
    }
}
