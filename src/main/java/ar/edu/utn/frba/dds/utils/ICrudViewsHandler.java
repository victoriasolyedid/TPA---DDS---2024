package ar.edu.utn.frba.dds.utils;

import io.javalin.http.Context;

import java.io.IOException;

public interface ICrudViewsHandler {
    void index(Context context);
    // void mostrar(Context context);
    void crear(Context context) throws IOException;
    void guardar(Context context) throws IOException;
    // void editar(Context context);
    void actualizar(Context context);
    void eliminar(Context context);
}
