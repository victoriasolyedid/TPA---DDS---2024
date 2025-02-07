package ar.edu.utn.frba.dds.models.entities.incidentes;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "alerta")
@NoArgsConstructor
public class Alerta extends Incidente {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoAlerta", nullable = false, columnDefinition = "VARCHAR(15)")
    public TipoAlerta tipoAlerta;

    public Alerta(Heladera heladera, TipoAlerta tipoAlerta) {
        super();
        this.heladera = heladera;
        this.tipoIncidente = TipoIncidente.ALERTA;
        this.tipoAlerta = tipoAlerta;
    }


    @Override
    public String toHtml(String tipoDeUso) {
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String fechaFormateada = this.fecha != null ? this.fecha.format(fechaFormatter) : "No disponible";
        String horaFormateada = this.hora != null ? this.hora.format(horaFormatter) : "No disponible";

        StringBuilder html = new StringBuilder();
        html.append("<td>").append(this.heladera).append("</td>");
        html.append("<td>").append(this.tipoIncidente.toString()).append(": ").append(this.tipoAlerta.toString()).append("</td>");
        html.append("<td>").append(fechaFormateada).append(" ").append(horaFormateada).append("</td>");
        // html.append("<td>").append(this.tecnicoAsignado.getNombre()).append("</td>");
        html.append("<td>").append(this.resuelto ? "Sí" : "No").append("</td>");
        return html.toString();
    }
}