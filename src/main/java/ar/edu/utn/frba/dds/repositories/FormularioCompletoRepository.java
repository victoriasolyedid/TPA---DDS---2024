package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class FormularioCompletoRepository implements WithSimplePersistenceUnit {

    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof FormularioCompleto) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

}
