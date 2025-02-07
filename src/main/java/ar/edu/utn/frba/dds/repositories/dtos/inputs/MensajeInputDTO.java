package ar.edu.utn.frba.dds.dtos.inputs;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.*;

@Getter
@Setter
@Builder
public class MensajeInputDTO {
    private Long id;
    private Colaborador colaborador;
    private String contenido;
}
