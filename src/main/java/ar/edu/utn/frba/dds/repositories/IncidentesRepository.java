package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;

public class IncidentesRepository implements WithSimplePersistenceUnit, IBaseRepository{
    @Override
    public List<Incidente> buscarTodos() {
        return entityManager().createQuery("from " + Incidente.class.getName(), Incidente.class).getResultList();
    }

    public List<Incidente> buscarUltimaSemana() {
        return entityManager().createQuery("from " + Incidente.class.getName() + " i where i.fechaHora > current_date - 7", Incidente.class).getResultList();
    }

    public List<Incidente> buscarPorHeladera(Long heladeraId) {
        return entityManager()
                .createQuery("SELECT i FROM " + Incidente.class.getName() + " i WHERE i.heladera.id = :heladeraId", Incidente.class)
                .setParameter("heladeraId", heladeraId)
                .getResultList();
    }

    @Override
    public Optional<Incidente> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Incidente.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Incidente) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Incidente) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Incidente) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
