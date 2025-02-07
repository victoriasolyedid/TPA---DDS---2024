package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;

import java.io.IOException;
import java.util.List;

public class RecomendadorDePuntos {
    private AdapterPuntosDeColocacionRec adapterPuntosDeColocacionRec;

    public List<Coordenadas> calcularPuntos(Coordenadas coordenadas, Integer radio) throws IOException{
        return adapterPuntosDeColocacionRec.ptosDeColocacionRecomendados(coordenadas, radio);
   }
}