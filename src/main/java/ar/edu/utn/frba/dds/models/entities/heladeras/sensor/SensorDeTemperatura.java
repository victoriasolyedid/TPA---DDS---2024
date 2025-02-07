package ar.edu.utn.frba.dds.models.entities.heladeras.sensor;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name="sensor_de_temperatura")
public class SensorDeTemperatura extends Persistente implements IMqttMessageListener {

    @OneToOne(mappedBy = "sensorDeTemperatura", cascade = CascadeType.ALL)
    private Heladera heladera;

    @Column(name="dateUltimaMedicion", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime dateUltimaMedicion;

    public SensorDeTemperatura(Heladera heladera) {
        this.heladera = heladera;
        this.dateUltimaMedicion = LocalDateTime.now();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("Recibí una temperatura desde " + topic + ": " + mqttMessage.toString());
        var temperatura = Double.parseDouble(mqttMessage.toString());
        this.dateUltimaMedicion = LocalDateTime.now();

        this.enviarTemperaturaActual(temperatura);
    }

    public void enviarTemperaturaActual(Double temperatura) throws MessagingException {
            heladera.recibirTemperatura(temperatura);
    }

    public Boolean ultimaRecepcionDentroDelRango() {
        if (this.dateUltimaMedicion == null) {
            return false;
        }
        Duration duracion = Duration.between(this.dateUltimaMedicion, LocalDateTime.now());
        return duracion.toMinutes() < 5;
    }

    // TODO: de momento esta en el main de HeladeraBroker
    /*public static void main(String[] args) {
        HeladeraController heladeraController = HeladeraController.getInstancia();

        HeladeraDTO heladeraEncontradaDTO = null;
        try {
            heladeraEncontradaDTO = heladeraController.buscar(args[0]).orElseThrow();
            Heladera heladeraEncontrada = TransformarHeladerasDTO.convertirDTOaEntidad(heladeraEncontradaDTO);

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
    }*/
}