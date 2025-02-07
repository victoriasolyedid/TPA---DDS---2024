package ar.edu.utn.frba.dds.models.entities.colaboraciones.donacionDeDinero;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "donacion_de_dinero")
public class DonacionDeDinero extends TipoDeColaboracion {

    @Column(name = "monto", columnDefinition = "INTEGER(10)")
    private Integer monto;

    @Column(name = "periodica")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean periodica;

    @Column(name = "frecuencia", columnDefinition = "VARCHAR(100)")
    private String frecuencia;

    /*public DonacionDeDinero() {
        this.frecuencia = 0;
    }*/

    @Override
    public String getNombreColab() {
        return "Donacion de Dinero";
    }

    @Override
    public double puntajeAsociado(){
        return monto * 0.5;
    }

    @Override
    public String toHtml(String tipoDeUso) { return ""; }
}
