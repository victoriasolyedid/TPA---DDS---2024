package ar.edu.utn.frba.dds.models.entities.formularios;

import ar.edu.utn.frba.dds.models.repositories.converters.StringListConverter;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "formulario")
public class Formulario {
    @Id
    @Column (name = "nombre", columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(name = "campos", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> campos;

    public Formulario(String nombre) {
        this.nombre = nombre;
        this.campos = new ArrayList<>();
    }

    public void agregarCampo(String campo) {
        this.campos.add(campo);
    }
}