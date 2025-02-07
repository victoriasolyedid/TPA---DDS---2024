package ar.edu.utn.frba.dds.models.entities.heladeras.sensor;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name="sensor_de_movimiento")
public class SensorDeMovimiento extends Persistente implements IMqttMessageListener{

    @OneToOne(mappedBy = "sensorDeMovimiento", cascade = CascadeType.ALL)
    private Heladera heladera;

    public SensorDeMovimiento(Heladera heladera) {
        this.heladera = heladera;
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("Recibí una alerta de robo desde " + topic + ": " + mqttMessage.toString());
        heladera.recibirAlertaMovimiento();
    }
}