package ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor;


import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;

import java.io.IOException;
import java.util.List;

public class AdapterSugeridorAPI implements AdapterHeladerasRec {

    public List<Heladera> heladerasRecomendadas(List<Localidad> zonasQueFrecuenta) throws IOException {
        SugeridorDeHeladerasAPI sugeridorDeHeladerasAPI = SugeridorDeHeladerasAPI.getInstance();
        return sugeridorDeHeladerasAPI.heladerasRecomendadas(zonasQueFrecuenta);
    }
}
