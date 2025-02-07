package ar.edu.utn.frba.dds.models.entities.formularios;

import ar.edu.utn.frba.dds.models.entities.info.ubicacion.LocalidadMapper;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.ProvinciaMapper;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.repositories.converters.MapStringConverter;
import lombok.*;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "formulario_completo")
public class FormularioCompleto extends Persistente {

    @Column(name = "camposCompletos", columnDefinition = "TEXT")
    @Convert(converter = MapStringConverter.class)
    private Map<String, String> camposCompletos;

    public FormularioCompleto() {
        this.camposCompletos = new HashMap<>();
    }

    public void completarCampo(String campo, String valor) {
        String valorFinal = valor;

        if (Objects.equals(campo, "localidad") || Objects.equals(campo, "localidad_jur")) {
            valorFinal = LocalidadMapper.getNombreLocalidad(valor);
        } else if (Objects.equals(campo, "provincia") || Objects.equals(campo, "provincia_jur")) {
            valorFinal = ProvinciaMapper.getNombreProvincia(valor);
        }

        if (Objects.equals(valorFinal, "Desconocida")) {
            this.camposCompletos.put(campo, valor);
        } else {
            this.camposCompletos.put(campo, valorFinal);
        }
    }
}