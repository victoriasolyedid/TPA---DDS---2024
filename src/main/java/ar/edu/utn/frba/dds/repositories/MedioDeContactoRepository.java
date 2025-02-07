package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.MedioDeContacto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class MedioDeContactoRepository implements WithSimplePersistenceUnit {

    public List<MedioDeContacto> buscarPorTecnico(Long id) {
        return entityManager()
                .createQuery(
                        "SELECT m FROM MedioDeContacto m JOIN FETCH m.tecnico WHERE m.tecnico.id = :idTecnico",
                        MedioDeContacto.class
                )
                .setParameter("idTecnico", id)
                .getResultList();
    }

}




