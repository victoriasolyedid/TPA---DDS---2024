package ar.edu.utn.frba.dds.dtos.outputs;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.*;

@Getter
@Setter
@Builder
public class MensajeOutputDTO {
    private Long id;
    private Colaborador colaborador;
    private String contenido;
}