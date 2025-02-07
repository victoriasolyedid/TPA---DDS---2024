package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ColaboradorRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Colaborador> buscarTodos() {
        return entityManager().createQuery("from " + Colaborador.class.getName(), Colaborador.class).getResultList();
    }

    @Override
    public Optional<Colaborador> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Colaborador.class, id));
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Colaborador) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Colaborador) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("El objeto a actualizar no puede ser nulo");
        }

        EntityTransaction tx = entityManager().getTransaction();
        try {
            tx.begin();

            if (o instanceof Colaborador) {
                System.out.println("Actualizando colaborador con ID: " + ((Colaborador) o).getId() + " y puntos: " + ((Colaborador) o).getPuntosAcumulados());

                // Actualiza el colaborador en la base de datos
                entityManager().merge(o);

                System.out.println("Colaborador actualizado con éxito en la base de datos.");
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización: " + o.getClass().getName());
            }

            // Confirmamos la transacción
            tx.commit();
        } catch (Exception e) {
            // Si hay una excepción, revertimos la transacción
            if (tx.isActive()) {
                tx.rollback();
                System.err.println("Transacción revertida debido a un error: " + e.getMessage());
            }
            throw e; // Lanza la excepción para que el error se maneje en otro nivel
        }
    }

    public Optional<Colaborador> buscarPorMail(String mail) {
        return entityManager().createQuery(
                        "SELECT c FROM Colaborador c WHERE c.mail = :mail", Colaborador.class)
                .setParameter("mail", mail)
                .getResultStream()
                .findFirst();
    }

    /*public Optional<Colaborador> buscarPorDocumento(String tipoDoc, String nroDoc) {
        return entityManager().createQuery(
                "SELECT c FROM Colaborador c JOIN FormularioCompleto f ON f.id = c.datosColaborador.id WHERE f.camposCompletos['tipo_documento'] LIKE '%' + :tipoDoc + '%' AND f.camposCompletos['numero_documento'] LIKE '%' + :numeroDoc + '%'", Colaborador.class)
                .setParameter("tipoDoc", tipoDoc)
                .setParameter("numeroDoc", nroDoc)
                .getResultStream()
                .findFirst();
    }*/
}
