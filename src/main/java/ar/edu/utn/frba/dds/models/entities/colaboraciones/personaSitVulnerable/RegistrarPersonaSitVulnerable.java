package ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;

import lombok.*;
import javax.persistence.*;
@Builder
@Entity
@NoArgsConstructor
@Table(name = "registrar_persona_sit_vulnerable")
public class RegistrarPersonaSitVulnerable extends TipoDeColaboracion {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "personaSitVulnerable_id", referencedColumnName = "id")
    private PersonaSitVulnerable persona;

    public RegistrarPersonaSitVulnerable(PersonaSitVulnerable persona) {
        this.persona = persona;
    }

    @Override
    public String getNombreColab() {
        return "Registro de Persona en Situación Vulnerable";
    }

    @Override
    public double puntajeAsociado(){
        return 2;
    }

    @Override
    public String toHtml(String tipoDeUso) { return ""; }
}
