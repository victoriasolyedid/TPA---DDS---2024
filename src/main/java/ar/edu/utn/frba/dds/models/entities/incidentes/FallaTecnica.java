package ar.edu.utn.frba.dds.models.entities.incidentes;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Table(name = "falla_tecnica")
@AllArgsConstructor
@Builder
public class FallaTecnica extends Incidente {


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", referencedColumnName="id")
    private Colaborador colaborador;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto; // aca iria la ruta

    public FallaTecnica(Heladera heladera, Colaborador colaborador, String descripcion, String foto) {
        super();
        this.heladera = heladera;
        this.tipoIncidente = TipoIncidente.FALLA_TECNICA;
        this.colaborador = colaborador;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public FallaTecnica() {

    }

    @Override
    public String toHtml(String tipoDeUso) {
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String fechaFormateada = this.fecha != null ? this.fecha.format(fechaFormatter) : "No disponible";
        String horaFormateada = this.hora != null ? this.hora.format(horaFormatter) : "No disponible";


        StringBuilder html = new StringBuilder();
        html.append("<td>").append(this.heladera).append("</td>");
        html.append("<td>").append(this.tipoIncidente.toString()).append("</td>");
        html.append("<td>").append(fechaFormateada).append(" ").append(horaFormateada).append("</td>");
        // html.append("<td>").append(this.tecnicoAsignado.getNombre()).append("</td>");
        html.append("<td>").append(this.resuelto ? "Sí" : "No").append("</td>");
        return html.toString();
    }

    @Override
    public String toString() {
        return "FallaTecnica{" +
                "heladera='" + getHeladera() + '\'' +
                ", colaborador=" + colaborador +
                ", descripcion=" + descripcion +
                ", resuelto=" + getResuelto() +
                '}';
    }
}
