package ar.edu.utn.frba.dds.models.entities.incidentes;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.reportes.HtmlConvertible;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter
@Entity
@Table(name = "incidente")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract  class Incidente extends Persistente implements HtmlConvertible  {

    @Column(name = "fechaIncidente", columnDefinition = "DATE", nullable = false)
    protected LocalDate fecha;

    @Column(name = "horaIncidente", columnDefinition = "TIME", nullable = false)
    protected LocalTime hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heladera_id", referencedColumnName = "id")
    protected Heladera heladera;

    @Column(name = "descripcion", nullable = true, columnDefinition = "TEXT")
    protected String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoIncidente", nullable = false, columnDefinition = "VARCHAR(15)")
    protected TipoIncidente tipoIncidente;

    @Column(name = "resuelto")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    protected Boolean resuelto;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", referencedColumnName = "id")
    private Tecnico tecnicoAsignado;


    public String toHtml(String tipoDeUso) {
        return null;
    }


}