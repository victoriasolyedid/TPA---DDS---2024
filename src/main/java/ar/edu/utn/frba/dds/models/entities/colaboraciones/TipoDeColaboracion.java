package ar.edu.utn.frba.dds.models.entities.colaboraciones;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import ar.edu.utn.frba.dds.reportes.HtmlConvertible;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TipoDeColaboracion implements HtmlConvertible {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @Column(name="fechaColaboracion", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    protected LocalDateTime fechaColaboracion;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "colaborador_id", referencedColumnName = "id" )
    protected Colaborador colaborador;

    public abstract String getNombreColab();

    public double puntajeAsociado(){
        return 0;
    };

    public Heladera getHeladera() {
        return null;
    }
}