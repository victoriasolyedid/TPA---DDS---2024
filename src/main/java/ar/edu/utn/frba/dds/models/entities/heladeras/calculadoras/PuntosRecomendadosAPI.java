package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.*;

import java.io.IOException;
import java.util.List;

public class PuntosRecomendadosAPI {
    private static PuntosRecomendadosAPI instancia = null;
    //private static final String urlAPI = "https://{url}/api/puntos-recomendados/";
    private static final String urlAPI = "https://136bcedb-853d-4c12-8300-43861790d9d1.mock.pstmn.io/api/";

    private Retrofit retrofit;

    private PuntosRecomendadosAPI() throws IOException {
        this.retrofit = new Retrofit.Builder().
                baseUrl(urlAPI).
                addConverterFactory(GsonConverterFactory.create()).
                build();
    }

    public static PuntosRecomendadosAPI getInstance() throws IOException {
        if (instancia == null) {
            instancia = new PuntosRecomendadosAPI();
        }
        return instancia;
    }

    public List<Coordenadas> puntosRecomendados(Coordenadas coordenadas, Integer radio) throws IOException {
        ServicioPuntosRecomendados servicioPuntosRecomendados = this.retrofit.create(ServicioPuntosRecomendados.class);
        Call<CoordenadasObtenidas> puntosRecomendados = servicioPuntosRecomendados.getPuntosRecomendados(coordenadas, radio);
        Response<CoordenadasObtenidas> respuestaPuntosRec = puntosRecomendados.execute();
        return respuestaPuntosRec.body().getPuntosRecomendados();
    }
}


