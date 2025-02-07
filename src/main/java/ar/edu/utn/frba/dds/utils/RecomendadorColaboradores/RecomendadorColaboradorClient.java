package ar.edu.utn.frba.dds.models.entities.RecomendadorColaboradores;

import ar.edu.utn.frba.dds.dtos.outputs.ColaboradorDTO;
import ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras.CoordenadasObtenidas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface RecomendadorColaboradorClient {
        @POST("/colaboradores")
        Call<Void> createColaboradores(@Body List<ColaboradorDTO> colaboradorDTO);
}
