package ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;

import lombok.*;
import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "oferta_producto_o_servicio")
public class OfertaProductosOServicios extends TipoDeColaboracion {

    @Column(name = "nombre", columnDefinition = "TEXT")
    private String nombre;

    @Column(name = "valorEnPuntos", columnDefinition = "INTEGER(5)")
    private Integer valorEnPuntos;

    @Column(name = "imagen", columnDefinition = "MEDIUMTEXT")
    private String imagen;

    @Embedded
    private Rubro rubro;

    @Override
    public String getNombreColab() {
        return "Oferta de Productos o Servicios";
    }

    @Override
    public String toHtml(String tipoDeUso) { return ""; }
}