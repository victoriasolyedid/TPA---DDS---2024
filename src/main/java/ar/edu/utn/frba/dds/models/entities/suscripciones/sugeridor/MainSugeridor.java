package ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import java.io.IOException;
import java.util.List;

public class MainSugeridor {
    public static void main(String[] args) throws IOException {
        SugeridorDeHeladerasAPI sugeridorDeHeladerasAPI = SugeridorDeHeladerasAPI.getInstance();
        Localidad palermo =  new Localidad("Palermo");
        Localidad villaDelParque =  new Localidad("Villa del Parque");
        List<Heladera> heladerasObtenidas = sugeridorDeHeladerasAPI.heladerasRecomendadas(List.of(villaDelParque, palermo));

        for (Heladera unaHeladera : heladerasObtenidas) {
            System.out.println("Heladera recomendada: " + unaHeladera.getNombre());
        }
    }
}
