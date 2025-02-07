package ar.edu.utn.frba.dds.models.entities.tarjetas;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name="permiso")
public class Permiso extends Persistente {

    @ManyToOne
    @JoinColumn(name="tarjeta_codigo",referencedColumnName="codigo")
    private Tarjeta tarjeta;
    
    @ManyToOne
    @JoinColumn(name="heladera_id",referencedColumnName="id")
    private Heladera heladeraAsociada;

    @Column(name="fecha", columnDefinition = "DATETIME")
    private LocalDateTime fecha;

    public Permiso(Heladera heladeraAsociada, LocalDateTime fecha) {
        this.heladeraAsociada = heladeraAsociada;
        this.fecha = fecha;
    }
}