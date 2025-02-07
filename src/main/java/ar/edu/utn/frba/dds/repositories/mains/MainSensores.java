package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.repositories.SensorMovimientoRepository;
import ar.edu.utn.frba.dds.models.repositories.SensorTemperaturaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public class MainSensores {
    private SensorMovimientoRepository sensorMovimientoRepositorio;
    private SensorTemperaturaRepository sensorTemperaturaRepositorio;

    public static void main (String[] args){
        MainSensores instance = new MainSensores();
//        instance.sensorTemperaturaRepositorio = SensorTemperaturaRepository.getInstancia();
//        instance.sensorMovimientoRepositorio = SensorMovimientoRepository.getInstancia();

        instance.guardarSensores();
    }

    @Transactional
    public void guardarSensores(){
        Coordenadas coordenadas = new Coordenadas(-34.603722, -58.381592);
        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");
        Ubicacion ubicacionMedrano = new Ubicacion("Buenos Aires", "2023", buenosAires, caba);

        Heladera heladera1 = new Heladera(ubicacionMedrano,50, LocalDateTime.now(),new ModeloHeladera(),"heladeraDePrueba");

        SensorDeMovimiento sensorMov = new SensorDeMovimiento(heladera1);
        SensorDeTemperatura sensorTemp = new SensorDeTemperatura(heladera1);

        this.sensorMovimientoRepositorio.guardar(sensorMov);
        this. sensorTemperaturaRepositorio.guardar(sensorTemp);
    }
}
