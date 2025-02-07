package ar.edu.utn.frba.dds.models.entities.heladeras.publisher;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class HeladeraPublisher {
    // Definimos los topics
    private static final String TOPIC_QUEDAN_N_VIANDAS = "QUEDAN_N_VIANDAS";
    private static final String TOPIC_FALTAN_N_VIANDAS = "FALTAN_N_VIANDAS";
    private static final String TOPIC_DESPERFECTO_HELADERA = "DESPERFECTO";
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = RandomStringUtils.randomNumeric(10);

    private Heladera heladera;

    public HeladeraPublisher(Heladera heladera) {
        this.heladera = heladera;
    }

    private MqttClient client;

    // Conecta al cliente con el broker
    public void connectToBroker() throws MqttException {
        client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
    }

    // TODO: sacar mas adelante (está para probar el Cron del sensor)
    public void publishPrueba() {
        System.out.println("ESTOY INTENTANDO PUBLICAR");
        String TOPIC_PRUEBA = "2024-mi-no-grupo-13/heladeras/temperatura/Medrano";
        publishMessage(TOPIC_PRUEBA, "HIZO EL CHEQUEO");
    }

    // Publica un mensaje en el topic correspondiente
    private void publishMessage(String topic, String content) {
        try {
            this.connectToBroker();
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(2); // Nivel de calidad del servicio
            client.publish(topic, message);
            System.out.println("Mensaje publicado: " + content);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
