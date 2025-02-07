package ar.edu.utn.frba.dds.models.entities.heladeras.sensor;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="modelo_heladera")
public class ModeloHeladera extends Persistente {
    @Column
    private String nombre;
    @Column
    private Double temperaturaMinima;
    @Column
    private Double temperaturaMaxima;

    public ModeloHeladera(String nombre, Double temperaturaMinima, Double temperaturaMaxima) {
        this.nombre = nombre;
        this.temperaturaMaxima = temperaturaMaxima;
        this.temperaturaMinima = temperaturaMinima;
    }
}