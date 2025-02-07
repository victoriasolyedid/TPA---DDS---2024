package ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Embeddable
public class Rubro {

    @Column(name = "rubro", columnDefinition = "TEXT")
    private String rubro;

    public Rubro(String rubro) {
        this.rubro = rubro;
    }

    public String toString() {
        return this.rubro;
    }
}
