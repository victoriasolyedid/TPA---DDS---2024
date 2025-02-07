package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;

import java.io.IOException;
import java.util.List;

public class Ejemplo {
    public static void main(String[] args) throws IOException {

        PuntosRecomendadosAPI puntosRecomendadosAPI = PuntosRecomendadosAPI.getInstance();
        List<Coordenadas> coordenadasObtenidas = puntosRecomendadosAPI.puntosRecomendados(new Coordenadas(405.0, 306.0), 5);

       for(Coordenadas unaCoordenada:coordenadasObtenidas){
            System.out.println("longitud: " + unaCoordenada.getLongitud().toString() + " latitud: " + unaCoordenada.getLatitud().toString());
        }
    }
}


