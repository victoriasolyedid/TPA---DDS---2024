package ar.edu.utn.frba.dds.models.entities.tecnicos;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@Entity
@Table(name = "trabajo_pendiente")
public class TrabajoPendiente extends Persistente {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", referencedColumnName="id")
    private Tecnico tecnico;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Heladera heladera;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id", referencedColumnName = "id")
    private Incidente incidente;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "resuelto")
    private Boolean resuelto;

    public TrabajoPendiente(Heladera heladeraEncontrada, Incidente incidente) {
        this.heladera = heladeraEncontrada;
        this.incidente = incidente;
    }

    public TrabajoPendiente(Tecnico tecnicoCercano, Optional<Heladera> heladeraEncontrada, Incidente incidente, String descripcion) {
        this.tecnico = tecnicoCercano;
        this.incidente = incidente;
        this.heladera = heladeraEncontrada.orElse(null);
        this.descripcion = descripcion;
        this.resuelto = false;
    }

    public String toString() {
        return "TrabajoPendiente{" +
                "heladera=" + heladera +
                ", incidente=" + incidente +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
