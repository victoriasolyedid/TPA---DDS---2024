package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public class MainHeladera {

    private HeladerasRepository heladeraRepositorio;

    public static void main (String[] args){
        MainHeladera instance = new MainHeladera();
        instance.heladeraRepositorio = ServiceLocator.instanceOf(HeladerasRepository.class);

        instance.guardarHeladera();
    }

    @Transactional
    public void guardarHeladera(){
        Coordenadas coordenadas = new Coordenadas(-34.603722, -58.381592);
        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");
        Ubicacion ubicacionMedrano = new Ubicacion("Buenos Aires", "2023", buenosAires, caba);

        Heladera heladera1 = new Heladera(ubicacionMedrano,50, LocalDateTime.now(),new ModeloHeladera(),"heladeraDePrueba2");

        this.heladeraRepositorio.guardar(heladera1);
    }
}
