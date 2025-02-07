package ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface ServicioHeladerasRecomendadas {

    @GET("heladeras-recomendadas")
    Call<HeladerasObtenidas> getHeladerasRecomendadas(@Query("zonas") List<Localidad> zonasQueFrecuenta);
}
