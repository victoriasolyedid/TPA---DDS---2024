package ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import java.util.List;

public class HeladerasObtenidas {
    private List<Heladera> heladerasRecomendadas;

    public List<Heladera> getHeladerasRecomendadas() {
        return heladerasRecomendadas;
    }

    public void setHeladerasRecomendadas(List<Heladera> heladerasRecomendadas) {
        this.heladerasRecomendadas = heladerasRecomendadas;
    }
}
