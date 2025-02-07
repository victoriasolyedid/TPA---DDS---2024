package ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor;


import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class SugeridorDeHeladerasAPI {

        private static final String urlAPI = "https://dcea7fdf-7c0a-4bc9-a386-5cb7672aeaac.mock.pstmn.io/api/";

        private static SugeridorDeHeladerasAPI instancia = null;
        private Retrofit retrofit;

        private SugeridorDeHeladerasAPI() throws IOException {
            this.retrofit = new Retrofit.Builder().
                    baseUrl(urlAPI).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        public static SugeridorDeHeladerasAPI getInstance() throws IOException {
            if (instancia == null) {
                instancia = new SugeridorDeHeladerasAPI();
            }
            return instancia;
        }

    public List<Heladera> heladerasRecomendadas(List<Localidad> zonasQueFrecuenta) throws IOException {
        ServicioHeladerasRecomendadas servicioHeladerasRecomendadas = this.retrofit.create(ServicioHeladerasRecomendadas.class);
        Call<HeladerasObtenidas> heladerasRecomendadas = servicioHeladerasRecomendadas.getHeladerasRecomendadas(zonasQueFrecuenta);
        Response<HeladerasObtenidas> respuestaHeladeras = heladerasRecomendadas.execute();
        return respuestaHeladeras.body().getHeladerasRecomendadas();
    }
    }



