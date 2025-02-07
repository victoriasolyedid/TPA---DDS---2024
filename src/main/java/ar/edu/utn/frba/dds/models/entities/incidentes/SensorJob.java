package ar.edu.utn.frba.dds.models.entities.incidentes;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.dtos.inputs.HeladeraDTO;
import ar.edu.utn.frba.dds.dtos.outputs.TransformarHeladerasDTO;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.publisher.HeladeraPublisher;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SensorJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        HeladeraController heladeraController = ServiceLocator.instanceOf(HeladeraController.class);
        Heladera heladeraEncontrada = null;

        try {
            // TODO: ver como pasarle por parametro el nombre
            heladeraEncontrada = heladeraController.buscarPorNombre("Medrano").orElseThrow();
            SensorDeTemperatura sensor = heladeraEncontrada.getSensorDeTemperatura();
            if(!sensor.ultimaRecepcionDentroDelRango()) {
                System.out.println("No se recibió una temperatura en los últimos 5 minutos");
                heladeraEncontrada.recibirAlertaFallaConexion();
            }
            HeladeraPublisher heladeraPub = new HeladeraPublisher(heladeraEncontrada);
            heladeraPub.publishPrueba();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}