package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;

import java.io.IOException;
import java.util.List;

public class AdapterApiEnDesarrollo implements AdapterPuntosDeColocacionRec {
   public List<Coordenadas> ptosDeColocacionRecomendados(Coordenadas coordenadas, Integer radio) throws IOException {
       //deberia envolver el método de la API en desarrollo para que encastre con la interfaz AdapterPuntosDeColocacionRec
       //y lo pueda utilizar el RecomendadorDePuntos
       PuntosRecomendadosAPI puntosRecomendadosAPI = PuntosRecomendadosAPI.getInstance();
       return puntosRecomendadosAPI.puntosRecomendados(coordenadas, radio);
   }
}