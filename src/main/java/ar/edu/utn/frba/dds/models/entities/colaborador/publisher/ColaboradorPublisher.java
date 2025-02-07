package ar.edu.utn.frba.dds.models.entities.colaborador.publisher;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ColaboradorPublisher {
    private static final String TOPIC_DISTRIBUCION_VIANDAS = "2024-mi-no-grupo-13/colaboracion/distribucion-viandas";
    private static final String BROKER = "tcp://test.mosquitto.org:1884";
    private static final String CLIENT_ID = RandomStringUtils.randomNumeric(10);

    private Colaborador colaborador;

    public ColaboradorPublisher(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    private MqttClient client;

    public void connectToBroker() throws MqttException {
        client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
    }

    public void publishQuiereDonarODistribuir() {
        publishMessage(TOPIC_DISTRIBUCION_VIANDAS, String.valueOf(this.colaborador.getTarjeta().getCodigo()));
    }

    private void publishMessage(String topic, String content) {
        try {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(2); // Nivel de calidad del servicio
            client.publish(topic, message);
            System.out.println("Mensaje publicado: " + content);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
