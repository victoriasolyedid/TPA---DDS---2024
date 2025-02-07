package ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.calculadoras.RecomendadorDePuntos;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;

import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hacerse_cargo_de_heladera")
public class HacerseCargoDeHeladera extends TipoDeColaboracion {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    public List<Coordenadas> conocerPtosDeColocacionRecomendados(RecomendadorDePuntos recomendadorDePuntos, Coordenadas coordenadas, Integer radio) throws IOException {
        return recomendadorDePuntos.calcularPuntos(coordenadas, radio);
    }

    @Override
    public String getNombreColab() {
        return "Hacerse cargo de Heladera";
    }

    @Override
    public double puntajeAsociado() {
        if (this.heladera.getActiva()) {
            return heladera.cantidadDeMesesEnFuncionamiento() * 5;
        } else return 0;
    }

    @Override
    public String toHtml(String tipoDeUso) {
        return "";
    }
}