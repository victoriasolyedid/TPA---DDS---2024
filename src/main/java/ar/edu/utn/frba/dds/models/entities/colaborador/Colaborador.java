package ar.edu.utn.frba.dds.models.entities.colaborador;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.controllers.SolicitudController;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.publisher.ColaboradorPublisher;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Mensaje;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.OfertaProductosOServicios;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.MedioDeContacto;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.repositories.Persistente;

import lombok.*;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Builder
@Getter
@AllArgsConstructor
@Entity
@Table(name="colaborador")
public class Colaborador extends Persistente {

    @Column(name = "esJuridica")
    @Convert(disableConversion = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean esJuridica;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "formularioCompleto_id", referencedColumnName = "id")
    private FormularioCompleto datosColaborador;

    @Column(name = "puntosAcumulados", columnDefinition = "DECIMAL")
    private Double puntosAcumulados;

    @Column(name = "mail", columnDefinition = "VARCHAR(256)")
    private String mail;

    @OneToMany(mappedBy = "colaborador")
    private List<TipoDeColaboracion> colaboraciones;

    @OneToMany(mappedBy = "colaborador")
   // @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OfertaProductosOServicios> productosOServiciosObtenidos;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", referencedColumnName = "id")
    private List<Localidad> zonasQueFrecuenta;

    @Nullable
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_codigo", referencedColumnName = "codigo")
    private Tarjeta tarjeta;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedioDeContacto> medioDeContacto;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vianda> viandas;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Solicitud> solicitudes;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Suscripcion> suscripciones;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mensaje> mensajes;

    @Transient
    private ColaboradorPublisher colaboradorPublisher;

    @Transient
    private SolicitudController solicitudController = ServiceLocator.instanceOf(SolicitudController.class);

    @Transient
    private HeladeraController heladeraController = ServiceLocator.instanceOf(HeladeraController.class);

    public Colaborador(String mail, Boolean esJuridica) {
        this.mail = mail;
        this.esJuridica = esJuridica;
        this.puntosAcumulados = (double) 0;
        this.colaboraciones = new ArrayList<>();
        this.zonasQueFrecuenta = new ArrayList<>();
        this.medioDeContacto = new ArrayList<>();
        this.colaboradorPublisher = new ColaboradorPublisher(this);
        this.suscripciones = new ArrayList<>();
        this.datosColaborador = new FormularioCompleto();
        this.tarjeta = new Tarjeta(this);
    }

    public Colaborador(String mail, Boolean esJuridica, FormularioCompleto datosColaborador) {
        this.mail = mail;
        this.esJuridica = esJuridica;
        this.puntosAcumulados = (double) 0;
        this.colaboraciones = new ArrayList<>();
        this.zonasQueFrecuenta = new ArrayList<>();
        this.medioDeContacto = new ArrayList<>();
        this.colaboradorPublisher = new ColaboradorPublisher(this);
        this.suscripciones = new ArrayList<>();
        this.datosColaborador = datosColaborador;
        this.tarjeta = new Tarjeta(this);
    }

    // colaborador con mail, esJuridica y formulario completo

    public Colaborador() {
        this.puntosAcumulados = (double) 0;
        this.colaboraciones = new ArrayList<>();
        this.zonasQueFrecuenta = new ArrayList<>();
        this.medioDeContacto = new ArrayList<>();
        this.colaboradorPublisher = new ColaboradorPublisher(this);
        this.tarjeta = new Tarjeta(this);
    }

    public Colaborador(List<Localidad> zonasQueFrecuenta) {
        this.zonasQueFrecuenta = zonasQueFrecuenta;
        this.puntosAcumulados = (double) 0;
        this.colaboraciones = new ArrayList<>();
        this.medioDeContacto = new ArrayList<>();
        this.colaboradorPublisher = new ColaboradorPublisher(this);
        this.tarjeta = new Tarjeta(this);
    }

    public String toString() {
        if (esJuridica) {
            return this.datosColaborador.getCamposCompletos().get("razon_social");
        }
        String nombre = this.datosColaborador.getCamposCompletos().get("nombre");
        String apellido = this.datosColaborador.getCamposCompletos().get("apellido");
        return nombre + " " + apellido;
    }

    public String getNombre() { // FIXME
        return this.toStringNom();
    }

    public String toStringNom() {
        if (esJuridica) {
            return this.datosColaborador.getCamposCompletos().get("razon_social");
        }
        return this.datosColaborador.getCamposCompletos().get("nombre");
    }

    public void setPuntosAcumulados(Double nuevosPuntos) {
        this.puntosAcumulados = puntosAcumulados + nuevosPuntos;
    }

    public void agregarColaboracion(TipoDeColaboracion colaboracion) {
        Heladera heladera = colaboracion.getHeladera();
        if(heladera != null) {
            System.out.println("La heladera asociada es "+ heladera.getNombre());
            //agregarZona(heladera.getUbicacion().getLocalidad());
            //heladeraController.verificarPermisosParaAbrir(colaboracion,this);
        } else {
            colaboraciones.add(colaboracion);
        }
    }

    public double puntosCanjeados() {
       return productosOServiciosObtenidos.stream().mapToDouble(OfertaProductosOServicios::getValorEnPuntos).sum();
    }

    public Boolean estaEnZonaQueFrecuenta(Heladera heladera){
        return this.zonasQueFrecuenta.contains(heladera.getUbicacion().getLocalidad());
    }

    public void agregarZona(Localidad zona) {
        zonasQueFrecuenta.add(zona);
    }

    public void agregarMedioDeContacto(MedioDeContacto medio) {
        medioDeContacto.add(medio);
    }
    public void agregaSuscripcion(Suscripcion suscripcion) {
        suscripciones.add(suscripcion);
    }
    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    public Boolean tieneTarjeta() {
        return this.tarjeta != null;
    }

    public void generarSolicitudApertura (TipoDeColaboracion colaboracion, Solicitud solicitud) {
        Heladera heladera = colaboracion.getHeladera();
        if (heladera!= null){
            solicitudController.guardar(solicitud);
            solicitudController.agregarPermiso(this,heladera, solicitud.getFechaYHora());
           // colaboradorPublisher.publishQuiereDonarODistribuir();
        }
    }


}

