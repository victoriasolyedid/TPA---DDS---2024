package ar.edu.utn.frba.dds.dtos.inputs;

import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeladeraDTO {
    //private String id;
    private Ubicacion ubicacion;
    private Integer capacidad;
    private LocalDateTime fechaFuncionamiento;
    private SensorDeTemperatura sensorDeTemperatura;
    private SensorDeMovimiento sensorDeMovimiento;
    private Boolean activa;
    private ModeloHeladera rangoTemperatura;
    private Double temperaturaActual;
    private String nombre;
    private Integer cantViandas;
}
