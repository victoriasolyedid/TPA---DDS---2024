package ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class SugeridorDeHeladeras {
    private AdapterHeladerasRec adapterHeladerasRec;

    public List<Heladera> obtenerHeladeras (List<Localidad> zonasQueFrecuenta) throws IOException {
        return adapterHeladerasRec.heladerasRecomendadas(zonasQueFrecuenta);
    }
}
