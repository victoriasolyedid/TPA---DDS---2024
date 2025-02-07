package ar.edu.utn.frba.dds.dtos.outputs;

import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.MedioDeContacto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ColaboradorDTO {

    private Long id;
    private String nroDocumento;
    private String tipoDocumento;
    private String nombre;
    private String apellido;
    private Integer donaciones;
    private Double puntos;
    private List<MedioDeContactoDTO> mediosDeContacto;
}
