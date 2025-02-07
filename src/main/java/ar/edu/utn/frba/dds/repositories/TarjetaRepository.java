package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;

public class TarjetaRepository implements WithSimplePersistenceUnit, IBaseRepository {


    @Override
    public List buscarTodos() {
        return List.of();
    }

    @Override
    public Object buscar(Long id) {
        return null;
    }

    @Override
    public void guardar(Object o) {

    }

    @Override
    public void actualizar(Object o) {
            withTransaction(() -> {
                if (o instanceof Tarjeta) {
                    entityManager().merge(o);
                } else {
                    throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
                }
            });
    }

    @Override
    public void eliminar(Object o) {

    }

    public Optional<Tarjeta> buscarTarjetaPorColaboradorId(Long colaboradorId) {
        return entityManager()
                .createQuery(
                        "SELECT t FROM Tarjeta t WHERE t.colaborador.id = :colaboradorId",
                        Tarjeta.class
                )
                .setParameter("colaboradorId", colaboradorId)
                .getResultStream()
                .findFirst();
    }
}
