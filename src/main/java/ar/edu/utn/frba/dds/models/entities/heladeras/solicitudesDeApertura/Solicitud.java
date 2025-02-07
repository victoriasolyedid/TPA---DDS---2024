package ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name="solicitud")
public class Solicitud extends Persistente {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="colaborador_id", referencedColumnName = "id")
    private Colaborador colaborador;

    @Column(name="fechaYHora", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fechaYHora;

    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "colaboracion_id", referencedColumnName = "id")
    private TipoDeColaboracion colaboracion;

    public Solicitud(Colaborador colaborador, TipoDeColaboracion colaboracion, Heladera heladera) {
        this.colaborador = colaborador;
        this.fechaYHora = LocalDateTime.now();
        this.colaboracion = colaboracion;
        this.heladera = heladera;
    }
}
