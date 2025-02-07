package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Suscripcion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;

public class SuscripcionRepository implements IBaseRepository, WithSimplePersistenceUnit {

    HeladerasRepository heladerasRepository = ServiceLocator.instanceOf(HeladerasRepository.class);

    @Override
    public List<Solicitud> buscarTodos() {
        return entityManager().createQuery("from " + Suscripcion.class.getName(), Solicitud.class).getResultList();
    }

    @Override
    public Optional<Suscripcion> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Suscripcion.class, id));
    }

    public List<Suscripcion> buscarSuscripcionesPorHeladera(Heladera heladera) {
        return entityManager()
                .createQuery("FROM Suscripcion s WHERE s.heladera = :heladera", Suscripcion.class)
                .setParameter("heladera", heladera)
                .getResultList();
    }

    public List<Suscripcion> buscarSuscripcionesPorUsuario(Long colaboradorId) {
        return entityManager()
                .createQuery("FROM Suscripcion s WHERE s.colaborador.id = :colaboradorId", Suscripcion.class)
                .setParameter("colaboradorId", colaboradorId)
                .getResultList();
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Suscripcion) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Suscripcion) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Suscripcion) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}

