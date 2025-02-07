package ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.reportes.HtmlConvertible;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="donacion_de_viandas")
public class DonacionDeViandas extends TipoDeColaboracion implements HtmlConvertible {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="vianda_id", referencedColumnName = "id")
    private List<Vianda> viandas;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="heladera_id", referencedColumnName = "id")
    private Heladera heladera;

    @Column(name="cantidadViandas", columnDefinition = "INTEGER(4)")
    private Integer cantViandas;

    public DonacionDeViandas(List<Vianda> viandas, Heladera heladera, Integer cantViandas, Colaborador colaborador) {
        this.viandas = viandas;
        this.heladera = heladera;
        this.cantViandas = cantViandas;
        this.colaborador = colaborador;
    }

    public DonacionDeViandas() {
        this.viandas = new ArrayList<>();
    }

    public Integer obtenerCantidad() {
        return viandas.size();
    }

    public void agregarViandas(Vianda vianda) {
        viandas.add(vianda);
    }

    @Override
    public double puntajeAsociado() {
        return this.obtenerCantidad() * 0.5;
    }

    public void entregarTarjeta(){
        Colaborador colaborador = getColaborador();

        if(colaborador.tieneTarjeta()){
            colaborador.setTarjeta(new Tarjeta(colaborador));
        }
    }

    @Override
    public String getNombreColab() {
        return "Donacion de Vianda";
    }

    @Override
    public String toHtml(String tipoDeUso) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String fechaHoraFormateada = this.getFechaColaboracion().format(formatter);
        StringBuilder html = new StringBuilder();

        switch (tipoDeUso) {
            case "ViandasPorHeladera":
                html.append("<td>").append(this.heladera.toString()).append("</td>");
                html.append("<td>").append("Donación de Viandas").append("</td>");
                html.append("<td>").append("Ingresaron: ").append(this.cantViandas).append(" viandas").append("</td>");
                html.append("<td>").append(this.getColaborador().toString()).append("</td>");
                html.append("<td>").append(fechaHoraFormateada).append("</td>");
                break;
            case "ViandasPorColaborador":
                html.append("<td>").append(this.getColaborador()).append("</td>");
                html.append("<td>").append(this.cantViandas).append("</td>");
                html.append("<td>").append("-").append("</td>");
                html.append("<td>").append(fechaHoraFormateada).append("</td>");
                break;
            default:
                html.append("<td>Error: Tipo de uso no válido</td>");
                break;
        }
        return html.toString();
    }
}
