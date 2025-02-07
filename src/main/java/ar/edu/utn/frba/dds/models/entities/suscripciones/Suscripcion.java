package ar.edu.utn.frba.dds.models.entities.suscripciones;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="suscripcion")
public class Suscripcion extends Persistente {

    @ManyToOne
    @JoinColumn(name="colaborador_id",referencedColumnName="id")
    private Colaborador colaborador;


    @Column(name="cantidadViandasDeseadas", columnDefinition = "INTEGER(4)")
    private int cantidadViandasDeseadas;

    @ManyToOne
    @JoinColumn(name="heladera_id",referencedColumnName="id")
    private Heladera heladera;

    public Suscripcion(Colaborador colaborador, int cantidadViandasDeseadas, Heladera heladera) {
        this.heladera = heladera;
        this.colaborador = colaborador;
        this.cantidadViandasDeseadas = cantidadViandasDeseadas;
    }

    public CharSequence getCantidadViandasDeseadas() {
        return String.valueOf(cantidadViandasDeseadas);
    }

    public Integer getCantViandasDeseadas() {
        return cantidadViandasDeseadas;
    }


}

