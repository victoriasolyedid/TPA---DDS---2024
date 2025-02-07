package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.MedioDeContacto;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.TipoMedioContacto;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.repositories.TecnicosRepository;

import javax.transaction.Transactional;

public class MainTecnicos {

    private TecnicosRepository tecnicoRepositorio;

    public static void main (String[] args){
        MainTecnicos instance = new MainTecnicos();
        instance.tecnicoRepositorio = ServiceLocator.instanceOf(TecnicosRepository.class);

        instance.guardarTecnico();
    }

    @Transactional
    public void guardarTecnico(){
        MedioDeContacto whatsapp = new MedioDeContacto(TipoMedioContacto.WHATSAPP,"15328745");
        Tecnico tecnico1 = new Tecnico("Lucas","Saclier", TipoDocumento.DNI,"44376298","20443762982",whatsapp);

        this.tecnicoRepositorio.guardar(tecnico1);
    }
}
