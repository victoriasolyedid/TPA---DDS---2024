package ar.edu.utn.frba.dds.models.entities.incidentes;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import io.javalin.http.Context;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.UUID;

public class HeladeraBroker2 {

    public void iniciarBroker(Context ctx) {

        Usuario user = ctx.sessionAttribute("user");
        Colaborador colaborador = user.getColaborador();

        HeladerasRepository heladerasRepository = ServiceLocator.instanceOf(HeladerasRepository.class);
        List<Heladera> heladerasDelColaborador = heladerasRepository.buscarHeladerasPorColaborador(colaborador.getId());

        String broker = "tcp://broker.hivemq.com:1883";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // Conectar al broker MQTT

            MqttClient sensorMovimientoCliente = new MqttClient(broker, "sensorMovimiento_" + UUID.randomUUID(), persistence);
            MqttClient sensorTemperaturaCliente = new MqttClient(broker, "sensorTemperatura_" + UUID.randomUUID(), persistence);
            MqttClient heladeraCliente = new MqttClient(broker, "heladera_" + UUID.randomUUID(), persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Conectando al broker: " + broker);
            sensorMovimientoCliente.connect(connOpts);
            sensorTemperaturaCliente.connect(connOpts);
            heladeraCliente.connect(connOpts);
            System.out.println("Conectado");

            for (Heladera heladera : heladerasDelColaborador) {
                String nombreNormalizado = heladera.getNombre().replace(" ", "");
                System.out.println(nombreNormalizado);
                String topicTemperatura = "2024-mi-no-grupo-13/heladeras/temperatura/" +nombreNormalizado;
                String topicMovimiento = "2024-mi-no-grupo-13/heladeras/movimiento/" +nombreNormalizado;
                String topicDistribucion = "2024-mi-no-grupo-13/colaboracion/distribucion-viandas";

                System.out.println("Suscripción al tópico: " + topicTemperatura);
                System.out.println("Suscripción al tópico: " + topicMovimiento);
                System.out.println("Suscripción al tópico: " + topicDistribucion);

                // Obtener los sensores de la heladera
                SensorDeTemperatura receptorTemperaturas = heladera.getSensorDeTemperatura();
                SensorDeMovimiento receptorMovimiento = heladera.getSensorDeMovimiento();

                System.out.println("Nos suscribimos a los topics de la heladera: " + heladera.getNombre());
                sensorTemperaturaCliente.subscribe(topicTemperatura, receptorTemperaturas);
                sensorMovimientoCliente.subscribe(topicMovimiento, receptorMovimiento);
                heladeraCliente.subscribe(topicDistribucion, heladera);
            }
            System.out.println("Bien! Nos suscribimos a las heladeras del colaborador.");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
