package ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;

import java.io.IOException;
import java.util.List;

public interface AdapterPuntosDeColocacionRec {
   public List<Coordenadas> ptosDeColocacionRecomendados (Coordenadas coordenadas, Integer radio) throws IOException;
}