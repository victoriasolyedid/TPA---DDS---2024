package ar.edu.utn.frba.dds.models.entities.tecnicos;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visita")
public class Visita extends Persistente {
    @ManyToOne
    @JoinColumn(name = "tecnico_id", referencedColumnName = "id")
    private Tecnico tecnico;

    @Column(name = "fechaVisita", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fecha;

    @OneToOne
    @JoinColumn(name = "heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;

    @Column(name = "resuelto")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean resuelto;

    public Visita(Heladera heladera, String descripcion, String foto, Boolean resuelto) {
        this.fecha = LocalDateTime.now();
        this.heladera = heladera;
        this.descripcion = descripcion;
        this.foto = foto;
        this.resuelto = resuelto;
    }

    public Visita() {
    }
}
