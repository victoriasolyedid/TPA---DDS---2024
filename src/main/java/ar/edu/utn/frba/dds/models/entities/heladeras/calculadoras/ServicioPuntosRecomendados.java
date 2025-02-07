package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicioPuntosRecomendados {
    @GET("puntos-recomendados")
    Call<CoordenadasObtenidas> getPuntosRecomendados(@Query("coordenadas") Coordenadas coordenadas, @Query("radio") Integer radio);
}