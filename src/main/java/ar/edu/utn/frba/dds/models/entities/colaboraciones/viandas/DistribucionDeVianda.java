package ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.reportes.HtmlConvertible;

import lombok.*;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name="distribucion_de_vianda")
public class DistribucionDeVianda extends TipoDeColaboracion implements HtmlConvertible {

    public DistribucionDeVianda(Heladera heladeraOrigen, Heladera heladeraDestino, Integer cantViandas, MotivoDistribucion motivo, Colaborador colaborador) {
        super();
        this.heladeraOrigen = heladeraOrigen;
        this.heladeraDestino = heladeraDestino;
        this.cantViandas = cantViandas;
        this.motivo = motivo;
        this.colaborador = colaborador;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="heladeraOrigen_id", referencedColumnName = "id")
    private Heladera heladeraOrigen;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="heladeraDestino_id", referencedColumnName = "id")
    private Heladera heladeraDestino;

    @Column(name="cantidadViandas", columnDefinition = "INTEGER(4)")
    private Integer cantViandas;

    @Enumerated (EnumType.STRING)
    @Column(name="motivo", nullable = false)
    private MotivoDistribucion motivo;

    @Override
    public double puntajeAsociado(){
        return cantViandas * 0.5;
    }

    public void entregarTarjeta(){
        Colaborador colaborador = getColaborador();

        if(colaborador.tieneTarjeta()){
            colaborador.setTarjeta(new Tarjeta(colaborador));
        }
    }

    @Override
    public String getNombreColab() {
        return "Distribución de Viandas";
    }

    @Override
    public String toHtml(String tipoDeUso) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String fechaHoraFormateada = this.getFechaColaboracion().format(formatter);
        StringBuilder html = new StringBuilder();

        switch (tipoDeUso) {
            case "ViandasPorHeladera":
                html.append("<td>").append(this.heladeraOrigen).append("</td>");
                html.append("<td>").append("Distribución de viandas").append("</td>");
                html.append("<td>").append("Salieron: ").append(this.cantViandas).append(" viandas").append("</td>");
                html.append("<td>").append(this.getColaborador()).append("</td>");
                html.append("<td>").append(fechaHoraFormateada).append("</td>");
                html.append("</tr>");

                html.append("<tr>");
                html.append("<td>").append(this.heladeraDestino).append("</td>");
                html.append("<td>").append("Distribución de viandas").append("</td>");
                html.append("<td>").append("Ingresaron: ").append(this.cantViandas).append(" viandas").append("</td>");
                html.append("<td>").append(this.getColaborador()).append("</td>");
                html.append("<td>").append(fechaHoraFormateada).append("</td>");
                break;

            case "ViandasPorColaborador":
                html.append("<td>").append(this.getColaborador()).append("</td>");
                html.append("<td>").append("-").append("</td>");
                html.append("<td>").append(this.cantViandas).append("</td>");
                html.append("<td>").append(fechaHoraFormateada).append("</td>");
                break;

            default:
                html.append("<td>Error: Tipo de uso no válido</td>");
        }
        return html.toString();
    }
}