package ar.edu.utn.frba.dds.models.entities.info.ubicacion;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Entity
@Table(name="localidad")
public class Localidad extends Persistente {

    @Column(name="localidad", columnDefinition = "VARCHAR(50)")
    private String localidad;

    public Localidad(String localidad) {
        this.localidad = localidad;
    }
}
