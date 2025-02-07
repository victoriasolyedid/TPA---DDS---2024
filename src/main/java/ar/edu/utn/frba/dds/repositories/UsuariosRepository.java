package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UsuariosRepository implements WithSimplePersistenceUnit, IBaseRepository {
    @Override
    public List<Usuario> buscarTodos() {
        return entityManager().createQuery("from " + Usuario.class.getName(), Usuario.class).getResultList();
    }

    @Override
    public Optional<Usuario> buscar(Long id) {
        return Optional.ofNullable(entityManager().find(Usuario.class, id));
    }

    public Optional<Usuario> buscarPorUsername(String id) {
        return Optional.ofNullable(entityManager().find(Usuario.class, id));
    }

    public Usuario buscarPorNombre(String nombreUsuario) {
        System.out.println("Buscando usuario por nombre: " + nombreUsuario);
        TypedQuery<Usuario> query = entityManager().createQuery("FROM Usuario WHERE nombreUsuario = :nombreUsuario", Usuario.class);
        query.setParameter("nombreUsuario", nombreUsuario);
        Usuario usuario = null;
        try {
            usuario = query.getSingleResult();
            System.out.println("Usuario encontrado: " + usuario);
        } catch (NoResultException e) {
            System.out.println("No se encontró ningún usuario con el nombre: " + nombreUsuario);
        }
        return usuario;
    }

    public Optional<Usuario> buscarUsuarioPorNombre(String nombreUsuario) {
        TypedQuery<Usuario> query = entityManager().createQuery("FROM Usuario WHERE nombreUsuario = :nombreUsuario", Usuario.class);
        query.setParameter("nombreUsuario", nombreUsuario);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void guardar(Object o) {
        withTransaction(() -> {
            if (o instanceof Usuario) {
                entityManager().persist(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para guardado");
            }
        });
    }

    @Override
    public void eliminar(Object o) {
        withTransaction(() -> {
            if (o instanceof Usuario) {
                entityManager().remove(entityManager().merge(o));
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para eliminación");
            }
        });
    }

    @Override
    public void actualizar(Object o) {
        withTransaction(() -> {
            if (o instanceof Usuario) {
                entityManager().merge(o);
            } else {
                throw new IllegalArgumentException("Tipo de objeto no soportado para actualización");
            }
        });
    }
}
