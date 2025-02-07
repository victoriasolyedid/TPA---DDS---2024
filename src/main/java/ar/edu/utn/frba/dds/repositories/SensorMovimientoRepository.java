package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;

public class SensorMovimientoRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<SensorDeMovimiento> buscarTodos() {
        return entityManager().createQuery("from " + SensorDeMovimiento.class.getName(), SensorDeMovimiento.class).getResultList();
    }

    @Override
    public Optional<SensorDeMovimiento> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(SensorDeMovimiento.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof SensorDeMovimiento) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof SensorDeMovimiento) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof SensorDeMovimiento) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
