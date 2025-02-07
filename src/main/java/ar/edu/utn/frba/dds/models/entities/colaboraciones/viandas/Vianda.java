package ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="vianda")
public class Vianda extends Persistente {

    @Column(name="comida", columnDefinition = "VARCHAR(100)")
    private String comida;

    @Column(name="fechaCaducidad", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fechaCaducidad;

    @Column(name="fechaDonacion", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fechaDonacion;

    @ManyToOne
    @JoinColumn(name="colaborador_id", referencedColumnName = "id")
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    @Column(name="calorias", columnDefinition = "INTEGER(4)")
    private Integer calorias;

    @Column(name="peso", columnDefinition = "INTEGER(4)")
    private Integer peso;

    @Column(name="entregado")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean entregado;

    public Vianda(String comida) {
        this.comida = comida;
        this.calorias = -1;
        this.peso = -1;
        this.entregado = false;
    }
}
