package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.OfertaProductosOServicios;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import java.util.Optional;

public class ColaboracionRepository implements WithSimplePersistenceUnit, IBaseRepository {

    @Override
    public List<TipoDeColaboracion> buscarTodos() {
        return entityManager().createQuery("from " + TipoDeColaboracion.class.getName(), TipoDeColaboracion.class).getResultList();
    }

    public List<OfertaProductosOServicios> buscarProductosOServicios() {
        return entityManager().createQuery("from " + OfertaProductosOServicios.class.getName(), OfertaProductosOServicios.class).getResultList();
    }

    public List<TipoDeColaboracion> buscarUltimaSemana() {
        return entityManager().createQuery("from " + TipoDeColaboracion.class.getName() + " i where i.fechaColaboracion > current_date - 7", TipoDeColaboracion.class).getResultList();
    }

    public <T extends TipoDeColaboracion> List<T> buscarColaboracionesPorTipo(Long usuarioId, Class<T> tipoDeColaboracion) {
        return entityManager().createQuery("SELECT c FROM " + tipoDeColaboracion.getName() + " c WHERE c.colaborador.id = :usuarioId", tipoDeColaboracion)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
    }

    public Optional<OfertaProductosOServicios> buscarProductoOServicio(Long id) {
        return Optional.ofNullable(entityManager().find(OfertaProductosOServicios.class, id));
    }

    @Override
    public Optional<TipoDeColaboracion> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(TipoDeColaboracion.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof TipoDeColaboracion) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof TipoDeColaboracion) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof TipoDeColaboracion) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }

    // PARA LA API
    public Long getTotalCantidadViandasUltimoMes(Long colaboradorId) {
        return entityManager().createQuery(
                "select count(*) from DonacionDeViandas d " +
                        "where d.colaborador.id = :colaboradorId and d.fechaColaboracion > current_date - 30", Long.class
        ).setParameter("colaboradorId", colaboradorId).getSingleResult();
    }

    public Optional<Colaborador> buscarColaboradorPorColaboracionId(Long colaboracionId) {
        return entityManager()
                .createQuery("SELECT c.colaborador FROM TipoDeColaboracion c WHERE c.id = :colaboracionId", Colaborador.class)
                .setParameter("colaboracionId", colaboracionId)
                .getResultStream()
                .findFirst();
    }

    public Optional<Heladera> buscarHeladeraPorColaboracionId(Long colaboracionId) {
        return entityManager()
                .createQuery("SELECT c.heladera FROM DonacionDeViandas c WHERE c.id = :colaboracionId", Heladera.class)
                .setParameter("colaboracionId", colaboracionId)
                .getResultStream()
                .findFirst();
    }
}


