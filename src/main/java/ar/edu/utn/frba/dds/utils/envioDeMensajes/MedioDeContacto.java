package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="medio_de_contacto")
public class MedioDeContacto extends Persistente {

    @Enumerated(EnumType.STRING)
    @Column(name="tipoMedioContacto",nullable = false)
    private TipoMedioContacto tipo;

    @Column(name="valor",columnDefinition = "VARCHAR(25)")
    private String valor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="colaborador_id", referencedColumnName = "id")
    private Colaborador colaborador;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tecnico_id", referencedColumnName = "id")
    private Tecnico tecnico;


    public MedioDeContacto(TipoMedioContacto tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public MedioDeContacto(TipoMedioContacto tipo, String valor, Colaborador colaborador, Tecnico tecnico) {
        this.tipo = tipo;
        this.valor = valor;
        this.colaborador = colaborador;
        this.tecnico = tecnico;
    }
}