package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class HeladerasRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Heladera> buscarTodos() {
        return entityManager().createQuery("from " + Heladera.class.getName(), Heladera.class).getResultList();
    }

    @Override
    public Optional<Heladera> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Heladera.class, id));
    }

    public Optional<Heladera> buscarPorNombre(String nombre) {
        String query = "SELECT h FROM Heladera h WHERE h.nombre = :nombre";
        TypedQuery<Heladera> typedQuery = entityManager().createQuery(query, Heladera.class);
        typedQuery.setParameter("nombre", nombre);
        return typedQuery.getResultStream().findFirst();
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Heladera) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Heladera) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        System.out.println("que ondaa soy la heladera antes de actualizarse");
        withTransaction(() -> {
            if (o instanceof Heladera) {
                entityManager().merge(o);
                System.out.println("que ondaa soy la heladera actualizada");
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }

    public List<Heladera> buscarHeladerasPorColaborador(Long colaboradorId) {
        String query = "SELECT h FROM HacerseCargoDeHeladera hcc JOIN hcc.heladera h " +
                "WHERE hcc.colaborador.id = :colaboradorId";
        TypedQuery<Heladera> typedQuery = entityManager().createQuery(query, Heladera.class);
        typedQuery.setParameter("colaboradorId", colaboradorId);
        return typedQuery.getResultList();
    }
}
