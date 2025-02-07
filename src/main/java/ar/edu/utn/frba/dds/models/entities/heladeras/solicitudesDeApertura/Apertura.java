package ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Setter
@Table(name="apertura")
public class Apertura extends Persistente {
    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    @Column(name="fecha", columnDefinition = "DATETIME")
    private LocalDateTime fecha;

    public Apertura(Heladera heladera) {
        this.heladera = heladera;
        this.fecha = LocalDateTime.now();
    }
}
