package ar.edu.utn.frba.dds.models.entities.incidentes;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.quartz.*;

public class HeladeraBroker {

    public static void main(String[] args) throws SchedulerException {
        HeladeraController heladeraController = ServiceLocator.instanceOf(HeladeraController.class);

//        String topicTemperatura = "2024-mi-no-grupo-13/heladeras/temperatura/" + args[0];
//        String topicMovimiento = "2024-mi-no-grupo-13/heladeras/movimiento/" + args[0]
          String topicTemperatura = "2024-mi-no-grupo-13/heladeras/temperatura/heladeraDePrueba2" ;
          String topicMovimiento = "2024-mi-no-grupo-13/heladeras/movimiento/heladeraDePrueba2";

        String topicDistribucion = "2024-mi-no-grupo-13/colaboracion/distribucion-viandas";
        String topicSolicitud = "2024-mi-no-grupo-13/heladeras/solicitud";
        int qos = 2;
        String broker =  "tcp://test.mosquitto.org:1883";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sensorMovimientoCliente = new MqttClient(broker, RandomStringUtils.randomNumeric(10), persistence);
            MqttClient sensorTemperaturaCliente = new MqttClient(broker, RandomStringUtils.randomNumeric(10), persistence);
            MqttClient heladeraCliente = new MqttClient(broker, RandomStringUtils.randomNumeric(10), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Conectando al broker: " + broker);
            sensorMovimientoCliente.connect(connOpts);
            sensorTemperaturaCliente.connect(connOpts);
            heladeraCliente.connect(connOpts);
            System.out.println("Conectado");

           // Heladera heladeraEncontrada = heladeraController.buscarPorNombre(args[0]).get();
            Heladera heladeraEncontrada = heladeraController.buscarPorNombre("heladeraDePrueba2").get();

            System.out.println("Construyendo los receptores");
            SensorDeTemperatura receptorTemperaturas = heladeraEncontrada.getSensorDeTemperatura();
            SensorDeMovimiento receptorMovimiento = heladeraEncontrada.getSensorDeMovimiento();

            System.out.println("Nos suscribimos a los topics");
            sensorTemperaturaCliente.subscribe(topicTemperatura, receptorTemperaturas);
            sensorMovimientoCliente.subscribe(topicMovimiento, receptorMovimiento);
            heladeraCliente.subscribe(topicDistribucion, heladeraEncontrada);
            System.out.println("Bien! Nos suscribimos.");

            //TODO: Cortar la comunicacion cuando la heladera se desactive?

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

        // ---- Cron ---- //
        // https://www.baeldung.com/quartz
        // https://sg.com.mx/revista/18/una-introduccion-quartz-calendarizacion-tareas-java
//        try {
//            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//            Scheduler scheduler = schedulerFactory.getScheduler();
//
//            JobDetail job = JobBuilder.newJob(SensorJob.class)
//                    .withIdentity("ElSensorLaburante", "SensoresMedrano")
//                    .build();
//
//            Trigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity("SensorTrigger", "SensoresMedranoTriggers")
//                    .startNow()
//                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                            // .withIntervalInMinutes(5); // Cada 5 minutos
//                            .withIntervalInSeconds(20) // Cada 20 segundos para pruebas
//                            .repeatForever())
//                    .build();
//
//            // Registro dentro del Scheduler
//            scheduler.scheduleJob(job, trigger);
//            scheduler.start();
//        } catch (Exception e) {
//            System.out.println("Ocurrió una excepción");
//        }
    }
}
