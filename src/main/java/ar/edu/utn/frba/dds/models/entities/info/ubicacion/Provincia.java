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
@Table(name="provincia")
public class Provincia extends Persistente {
    @Column(name="provincia", columnDefinition = "VARCHAR(50)")
    private String provincia;

    public Provincia(String provincia) {
        this.provincia = provincia;
    }
}
