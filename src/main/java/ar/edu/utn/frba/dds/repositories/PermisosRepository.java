package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.tarjetas.Permiso;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class PermisosRepository implements WithSimplePersistenceUnit {

    public List<Permiso> buscarPermisosPorTarjetaId(Long tarjetaId) {
        return entityManager()
                .createQuery(
                        "SELECT p FROM Permiso p WHERE p.tarjeta.id = :tarjetaId",
                        Permiso.class
                )
                .setParameter("tarjetaId", tarjetaId)
                .getResultList();
    }

}
