package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;

public class SensorTemperaturaRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<SensorDeTemperatura> buscarTodos() {
        return entityManager().createQuery("from " + SensorDeTemperatura.class.getName(), SensorDeTemperatura.class).getResultList();
    }

    @Override
    public Optional<SensorDeTemperatura> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(SensorDeTemperatura.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof SensorDeTemperatura) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof SensorDeTemperatura) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof SensorDeTemperatura) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
