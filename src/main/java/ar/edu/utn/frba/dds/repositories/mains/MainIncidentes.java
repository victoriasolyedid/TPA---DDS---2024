package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.incidentes.Alerta;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoAlerta;
import ar.edu.utn.frba.dds.models.repositories.IncidentesRepository;

import javax.transaction.Transactional;

public class MainIncidentes {

    private IncidentesRepository incidentesRepositorio;

    public static void main (String[] args){
        MainIncidentes instance = new MainIncidentes();
        instance.incidentesRepositorio = ServiceLocator.instanceOf(IncidentesRepository.class);

        instance.guardarIncidente();
    }

    @Transactional
    public void guardarIncidente(){

        /*Colaborador colaborador1 = new Colaborador("juan.perez@example.com", false);
        Alerta incidente1 = new Alerta("Medrano", TipoAlerta.FRAUDE);

        this.incidentesRepositorio.guardar(incidente1);*/
    }
}
