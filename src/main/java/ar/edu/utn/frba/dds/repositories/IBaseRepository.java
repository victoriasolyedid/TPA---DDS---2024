package ar.edu.utn.frba.dds.models.repositories;

import java.util.List;

public interface IBaseRepository {
    public List buscarTodos();
    public Object buscar(Long id);
    public void guardar(Object o);
    public void actualizar(Object o);
    public void eliminar(Object o);
}