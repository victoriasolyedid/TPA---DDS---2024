package ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.tarjetas.UsoTarjeta;
import ar.edu.utn.frba.dds.models.repositories.Persistente;
import ar.edu.utn.frba.dds.models.entities.validador.CustomException;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.models.repositories.converters.LocalDateTimeAttributeConverter;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "persona_sit_vulnerable")
public class PersonaSitVulnerable extends Persistente {

    @Column(name = "nombre", columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(name = "apellido", columnDefinition = "VARCHAR(50)")
    private String apellido;

    @Column(name = "fechaNacimiento", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fechaNacimiento;

    @Column(name = "fechaRegistro", columnDefinition = "DATETIME")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fechaRegistro;

    @Column(name = "situacionCalle")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean situacionCalle;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id", referencedColumnName = "id")
    private Ubicacion domicilio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoDocumento")
    private TipoDocumento tipoDocumento;

    @Column(name = "documento", columnDefinition = "VARCHAR(10)")
    private String documento;

    @Column(name = "tieneMenoresACargo")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean tieneMenoresACargo;

    @Column(name = "menoresACargo", columnDefinition = "INTEGER(2)")
    private Integer menoresACargo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_codigo", referencedColumnName = "codigo")
    private Tarjeta tarjeta;

    @Column(name = "cantViandasRestantes", columnDefinition = "INTEGER(4)")
    private Integer cantViandasRestantes;

    public PersonaSitVulnerable(String nombre, LocalDateTime fechaNacimiento, LocalDateTime fechaRegistro, Boolean situacionCalle, Ubicacion domicilio, TipoDocumento tipoDocumento, String documento, Boolean tieneMenoresACargo, Integer menoresACargo, Tarjeta tarjeta) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.situacionCalle = situacionCalle;
        this.domicilio = domicilio;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.tieneMenoresACargo = tieneMenoresACargo;
        this.menoresACargo = menoresACargo;
        this.cantViandasRestantes = inicializarCantViandasRestantes();
    }

    public int inicializarCantViandasRestantes() {
        return (4 + this.menoresACargo * 2);
    }

    public void usarTarjeta (Heladera heladera) throws CustomException {
        if(this.cantViandasRestantes >=1) {
            this.cantViandasRestantes --;
            UsoTarjeta usoNuevo = new UsoTarjeta(LocalDateTime.now(),heladera);
            this.tarjeta.agregarUso(usoNuevo);
        }
        else {
            throw new CustomException("La tarjeta ha superado el límite de usos diarios.");
        }
    }
}
