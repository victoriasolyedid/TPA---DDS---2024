package ar.edu.utn.frba.dds.models.entities.heladeras;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.controllers.SolicitudController;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.controllers.IncidenteController;
import ar.edu.utn.frba.dds.controllers.TecnicoController;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Apertura;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoAlerta;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Permiso;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;

import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.Nullable;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="heladera")
public class Heladera extends Persistente implements IMqttMessageListener {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="ubicacion", referencedColumnName ="id")
    private Ubicacion ubicacion;

    @Column(name="capacidad", columnDefinition = "INTEGER(4)")
    private Integer capacidad;

    @Column(name="fechaFuncionamiento", columnDefinition = "DATE")
    private LocalDate fechaFuncionamiento;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorDeTemperatura_id", referencedColumnName = "id")
    @Nullable
    private SensorDeTemperatura sensorDeTemperatura;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorDeMovimiento_id", referencedColumnName = "id")
    @Nullable
    private SensorDeMovimiento sensorDeMovimiento;

    @Column(name="activa")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean activa = true;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_heladera_id", referencedColumnName = "id")
    private ModeloHeladera modelo;

    @Column(name="temperaturaActual", columnDefinition = "DECIMAL")
    private Double temperaturaActual;

    @Column(name="nombre", columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(name="cantActualViandas", columnDefinition = "INTEGER(4)")
    private Integer cantViandas;

    @OneToMany(mappedBy = "heladera",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vianda> viandas;

    @OneToMany(mappedBy = "heladeraAsociada", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Permiso> permisos;

    @OneToMany(mappedBy = "heladera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Solicitud> solicitudes;

    @OneToMany(mappedBy = "heladera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Apertura> aperturas;

    @Transient
    private IncidenteController incidenteController = ServiceLocator.instanceOf(IncidenteController.class);;

    @Transient
    private HeladeraController heladeraController = ServiceLocator.instanceOf(HeladeraController.class);

    @Transient
    private SolicitudController solicitudController = ServiceLocator.instanceOf(SolicitudController.class);;

    @Transient
    private HeladerasRepository heladerasRepository = ServiceLocator.instanceOf(HeladerasRepository.class);;
    public Heladera(Ubicacion ubicacion, Integer capacidad, LocalDateTime fechaFuncionamiento, ModeloHeladera modelo, String nombre) {
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.fechaFuncionamiento = LocalDate.from(fechaFuncionamiento != null ? fechaFuncionamiento : LocalDateTime.now());
        this.modelo = modelo;
        this.nombre = nombre;
        this.cantViandas = 0;
        // this.heladeraController = ServiceLocator.instanceOf(HeladeraController.class);
        // this.temperaturaActual = 999.9;
    }

    public String toString() {
        return this.nombre;
    }

//    @Override
//    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
//        System.out.println("Recibi la intencion de una colaboracion " + topic + ": " + mqttMessage.toString());
//
//        String codigoTarjeta = mqttMessage.toString();
//    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        this.recibirAlertaMovimiento();
    }

    public double cantidadDeMesesEnFuncionamiento() {
        return Period.between(fechaFuncionamiento, LocalDate.now()).getMonths();
    }

    public Tecnico buscarTecnicoCercano() {

        List<Tecnico> tecnicos = ServiceLocator.instanceOf(TecnicoController.class).buscarTodos();

        Optional<Heladera> heladera = heladerasRepository.buscarPorNombre(this.getNombre());

        if (heladera.isPresent()) {
            Ubicacion ubicacionHeladera = heladera.get().getUbicacion();

            List<Tecnico> tecnicosCercanos = tecnicos.stream()
                    .filter(tecnico -> tecnico.estaCerca(ubicacionHeladera.getLocalidad())) // Cerrar correctamente el paréntesis
                    .collect(Collectors.toList());
            tecnicosCercanos.sort(Comparator.comparing(Tecnico::cantidadDeTrabajosPendientes));

            return tecnicosCercanos.isEmpty() ? null : tecnicosCercanos.get(0);
        } else {
            return null;
        }
    }


    public void recibirAlertaMovimiento() throws MessagingException {
      heladeraController.desactivarHeladera(this,TipoAlerta.FRAUDE);
    }

    public void recibirAlertaFallaConexion() throws MessagingException {
        heladeraController.desactivarHeladera(this,TipoAlerta.FALLA_CONEXION);
    }

    public void recibirTemperatura(Double temperatura) throws MessagingException {
        System.out.println("Recibi la temperatura");
        this.temperaturaActual = temperatura;
        heladeraController.temperaturaFueraDeRango(this);
    }



}
