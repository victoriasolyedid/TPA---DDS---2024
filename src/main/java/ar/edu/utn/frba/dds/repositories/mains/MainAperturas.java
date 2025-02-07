package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Apertura;
import ar.edu.utn.frba.dds.models.repositories.AperturasRepository;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.SensorMovimientoRepository;
import ar.edu.utn.frba.dds.models.repositories.SensorTemperaturaRepository;

import java.time.LocalDateTime;

public class MainAperturas {

    private AperturasRepository aperturasRepositorio;
    private HeladerasRepository heladerasRepositorio;

    private SensorMovimientoRepository sensorMovimientoRepository;
    private SensorTemperaturaRepository sensorTemperaturaRepository;

    public static void main (String[] args){
        MainAperturas instance = new MainAperturas();
        instance.aperturasRepositorio = ServiceLocator.instanceOf(AperturasRepository.class);
        instance.heladerasRepositorio = ServiceLocator.instanceOf(HeladerasRepository.class);
        instance.sensorMovimientoRepository = ServiceLocator.instanceOf(SensorMovimientoRepository.class);
        instance.sensorTemperaturaRepository = ServiceLocator.instanceOf(SensorTemperaturaRepository.class);

        instance.guardarApertura();
    }

    private void guardarApertura() {
        Coordenadas coordenadas = new Coordenadas(-34.603722, -58.381592);
        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");
        Ubicacion ubicacionMedrano = new Ubicacion("Buenos Aires", "2023", buenosAires, caba);

        ModeloHeladera modeloHeladera = new ModeloHeladera("ChillPro", (double) 0, 10.0);
        Heladera heladera1 = new Heladera(ubicacionMedrano, 25, LocalDateTime.now(), modeloHeladera, "Medrano");
        Apertura apertura1 = new Apertura(heladera1);

        this.heladerasRepositorio.guardar(heladera1);
        this.aperturasRepositorio.guardar(apertura1);
    }
}
