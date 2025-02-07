package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import lombok.*;
import java.util.List;

@Getter @Setter
public class CoordenadasObtenidas {
    public List<Coordenadas> puntosRecomendados;

    public CoordenadasObtenidas(List<Coordenadas> puntosRecomendados) {
        this.puntosRecomendados = puntosRecomendados;
    }
}