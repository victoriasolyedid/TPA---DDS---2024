package ar.edu.utn.frba.dds.models.entities.tarjetas;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="usoTarjeta")
public class UsoTarjeta extends Persistente {

    @Column(name="fechaDeUso", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fechaDeUso;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "heladera_id", referencedColumnName = "id")
    private Heladera heladeraDondeSeUso;

    @ManyToOne
    @JoinColumn(name = "tarjeta_codigo", referencedColumnName = "codigo")
    private Tarjeta tarjeta;

    public UsoTarjeta(LocalDateTime fechaDeUso, Heladera heladeraDondeSeUso) {
        this.fechaDeUso = fechaDeUso;
        this.heladeraDondeSeUso = heladeraDondeSeUso;
    }
}
