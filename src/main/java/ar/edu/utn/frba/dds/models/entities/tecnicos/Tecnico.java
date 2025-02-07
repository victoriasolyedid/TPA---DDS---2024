package ar.edu.utn.frba.dds.models.entities.tecnicos;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.controllers.VisitaTecnicoController;
import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.MedioDeContacto;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor
@Table (name = "tecnico")
public class Tecnico extends Persistente {

    @Column (name = "nombre", columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column (name = "apellido", columnDefinition = "VARCHAR(50)")
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name="tipoDeDocumento",nullable = false)
    private TipoDocumento tipoDocumento;

    @Column (name = "numeroDocumento", columnDefinition = "VARCHAR(8)")
    private String numeroDocumento;

    @Column (name = "cuil", columnDefinition = "VARCHAR(15)")
    private String cuil;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tecnico_localidad",
            joinColumns = @JoinColumn(name = "tecnico_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    )
    private List<Localidad> areaDeCobertura;

    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrabajoPendiente> trabajosPendientes ;

    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Visita> visitas;

    @Column (name = "mail", columnDefinition = "VARCHAR(225)")
    private String mail;

    public Tecnico(String nombre, String apellido, TipoDocumento tipoDocumento, String numeroDocumento, String cuil, MedioDeContacto medioDeContacto) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.cuil = cuil;
        this.areaDeCobertura = new ArrayList<>();
        this.trabajosPendientes = new ArrayList<>();
        this.visitas = new ArrayList<>();
    }

    public Boolean estaCerca(Localidad localidad) {
        return areaDeCobertura.contains(localidad);
    }

    public Integer cantidadDeTrabajosPendientes() {
        return trabajosPendientes.size();
    }

    public void gestionarIncidente(Incidente incidente) {


    }

    // el tecnico debe atender el incidente para luego persistirlo
    public void atenderIncidente(TrabajoPendiente trabajo, Boolean resuelto) {
        registrarVisita(trabajo.getHeladera(), "Descripcion", "Foto", resuelto);
        if(resuelto) {
            trabajosPendientes.remove(trabajo);
        }
    }

    public void registrarVisita(Heladera heladera, String descripcion, String foto, Boolean resuelto) {
        Visita nuevaVisita = new Visita(heladera, descripcion, foto, resuelto);
        ServiceLocator.instanceOf(VisitaTecnicoController.class).crearVisita(nuevaVisita);
        visitas.add(nuevaVisita);
    }
}

