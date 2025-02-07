package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.donacionDeDinero.DonacionDeDinero;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.repositories.SolicitudesRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public class MainSolicitudes {

    private SolicitudesRepository solicitudesRepositorio;

    public static void main (String[] args){
        MainSolicitudes instance = new MainSolicitudes();
//        instance.solicitudesRepositorio = SolicitudesRepository.getInstancia();

        instance.guardarSolicitud();
    }

    @Transactional
    public void guardarSolicitud(){
        Colaborador colaborador1 = new Colaborador("juan.perez@example.com", false);

        DonacionDeDinero colab1 = new DonacionDeDinero(10000,false,"Ninguna");

        Coordenadas coordenadas = new Coordenadas(-34.603722, -58.381592);
        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");
        Ubicacion ubicacionMedrano = new Ubicacion("Buenos Aires", "2023", buenosAires, caba);

        Heladera heladera1 = new Heladera(ubicacionMedrano,50, LocalDateTime.now(),new ModeloHeladera(),"heladeraDePrueba");

        Solicitud solicitud1 = new Solicitud(colaborador1,colab1,heladera1);

        this.solicitudesRepositorio.guardar(solicitud1);
    }
}
