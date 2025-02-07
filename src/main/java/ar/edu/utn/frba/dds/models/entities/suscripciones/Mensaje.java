package ar.edu.utn.frba.dds.models.entities.suscripciones;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name="mensaje")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="colaborador_id",referencedColumnName="id")
    private Colaborador colaborador;

    @Column(name="contenido", columnDefinition = "TEXT")
    private String contenido;

    public Mensaje(Long id, Colaborador colaborador, String contenido) {
        this.colaborador = colaborador;
        this.contenido = contenido;
    }
}
