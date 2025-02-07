package ar.edu.utn.frba.dds.models.entities.info.ubicacion;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter @Getter @NoArgsConstructor @Embeddable
public class Coordenadas {
    @Column
    public Double latitud;
    @Column
    public Double longitud;

    public Coordenadas(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}