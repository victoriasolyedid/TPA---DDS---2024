package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.controllers.SolicitudController;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.donacionDeDinero.DonacionDeDinero;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.OfertaProductosOServicios;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable.PersonaSitVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable.RegistrarPersonaSitVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.colaborador.publisher.ColaboradorPublisher;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.MedioDeContacto;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.TipoMedioContacto;
import ar.edu.utn.frba.dds.models.entities.formularios.Formulario;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.incidentes.FallaTecnica;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Mensaje;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.models.entities.tecnicos.TrabajoPendiente;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.*;
import ar.edu.utn.frba.dds.roles.TipoRol;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainDePrueba {
    private ColaboracionRepository colaboracionRepository;
    private UsuariosRepository usuariosRepository;
    private FormulariosRepository formulariosRepository;
    private ColaboradorRepository colaboradorRepository;
    private SuscripcionRepository suscripcionRepository;
    private HeladerasRepository heladerasRepository;
    private TecnicosRepository tecnicoRepository;

    public static void main(String[] args) {
        MainDePrueba instance = new MainDePrueba();
        instance.colaboracionRepository = ServiceLocator.instanceOf(ColaboracionRepository.class);
        instance.usuariosRepository = ServiceLocator.instanceOf(UsuariosRepository.class);
        instance.formulariosRepository = ServiceLocator.instanceOf(FormulariosRepository.class);
        instance.colaboradorRepository = ServiceLocator.instanceOf(ColaboradorRepository.class);
        instance.suscripcionRepository = ServiceLocator.instanceOf(SuscripcionRepository.class);
        instance.heladerasRepository = ServiceLocator.instanceOf(HeladerasRepository.class);
        instance.tecnicoRepository = ServiceLocator.instanceOf(TecnicosRepository.class);

        /* ---------- UBICACIONES ---------- */

        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");

        Ubicacion ubicacion1 = new Ubicacion("Avenida San Martin", "4453", buenosAires, caba);
        ubicacion1.buscarCoordenadas();

        Ubicacion ubicacion2 = new Ubicacion("Cuenca", "3035", buenosAires, caba);
        ubicacion2.buscarCoordenadas();

        Ubicacion ubicacion3 = new Ubicacion("Mozart", "2300", buenosAires, caba);
        ubicacion2.buscarCoordenadas();

        /* ---------- VIANDAS ---------- */

        Vianda vianda1 = new Vianda("Milanesa con pure");
        Vianda vianda2 = new Vianda("Arroz");
        List<Vianda> viandas = new ArrayList<>();
        viandas.add(vianda1);
        viandas.add(vianda2);

        /* ---------- HELADERAS ---------- */

        ModeloHeladera modeloHeladera = new ModeloHeladera("ChillPro", -8.0, 6.0);
        ModeloHeladera modeloHeladera2 = new ModeloHeladera("IceBox Ultra", -12.0, 3.0);
        ModeloHeladera modeloHeladera3 = new ModeloHeladera("FrostFree X", -10.0, 4.0);

        Heladera heladera1 = new Heladera(ubicacion1, 20, LocalDateTime.now(), modeloHeladera, "Heladera Agronomia");
        instance.guardarHeladera(heladera1);
        //heladera1.getHeladeraController().inicializarSensoresHeladera(heladera1);
        Heladera heladera2 = new Heladera(ubicacion2, 50, LocalDateTime.now(), modeloHeladera2, "Heladera Villa del Parque");
        //heladera2.getHeladeraController().inicializarSensoresHeladera(heladera2);
        Heladera heladera3 = new Heladera(ubicacion3, 24, LocalDateTime.now(), modeloHeladera3, "Heladera UTN");
        instance.guardarHeladera(heladera3);
        //heladera3.getHeladeraController().inicializarSensoresHeladera(heladera3);

        /* ---------- COLABORADORES ---------- */

        FormularioCompleto formCompleto5 = new FormularioCompleto();
        formCompleto5.getCamposCompletos().put("razon_social", "De la Reina S.A.");
        formCompleto5.getCamposCompletos().put("rubro", "Perfumería");
        formCompleto5.getCamposCompletos().put("tipo_empresa", "Empresa");
        formCompleto5.getCamposCompletos().put("whatsapp_jur", "1122334455");
        formCompleto5.getCamposCompletos().put("telefono_jur", "41231234");
        formCompleto5.getCamposCompletos().put("calle_jur", "Cuenca");
        formCompleto5.getCamposCompletos().put("altura_jur", "3035");
        formCompleto5.getCamposCompletos().put("provincia_jur", "Ciudad Autónoma de Buenos Aires");
        formCompleto5.getCamposCompletos().put("localidad_jur", "Villa del Parque");

        Colaborador colaborador1 = new Colaborador("juan@gmail.com", false);
        Colaborador colaborador2 = new Colaborador("juan123@yahoo.com", false);
        Colaborador colaborador5 = new Colaborador("delareina@mail.com", true, formCompleto5);

        instance.guardarColaborador(colaborador1);
        instance.guardarColaborador(colaborador5);

        /* ---------- TARJETAS ---------- */

        Tarjeta tarjeta1 = new Tarjeta(colaborador2);

        /* ---------- COLABORACIONES ---------- */

        LocalDate fecha = LocalDate.of(1975, 9, 10); // yyyy-MM-dd
        LocalTime hora = LocalTime.of(14, 30, 0); // HH:mm:ss
        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        PersonaSitVulnerable persona1 = new PersonaSitVulnerable("Juana Perez", fechaHora, LocalDateTime.now(), true, ubicacion2, TipoDocumento.DNI, "24987236", false, 0, tarjeta1);

        DonacionDeDinero colab2 = new DonacionDeDinero(5000, true, "2");
        DonacionDeDinero colab3 = new DonacionDeDinero(7500, false, "0");
        HacerseCargoDeHeladera colab4 = new HacerseCargoDeHeladera(heladera2);
        RegistrarPersonaSitVulnerable colab5 = new RegistrarPersonaSitVulnerable(persona1);

        /* ------------------------------------ */

        instance.guardarColaboracion(colab2);
        instance.guardarColaboracion(colab3);
        instance.guardarColaboracion(colab4);
        instance.guardarColaboracion(colab5);

        /* ---------- FORMULARIOS ---------- */

        Formulario formHumana = new Formulario("formHumana");
        Formulario formJuridica = new Formulario("formJuridica");
        Formulario formAdmin = new Formulario("formAdmin");

        formHumana.setCampos(Arrays.asList("nombre", "apellido", "tipo_documento", "numero_documento", "whatsapp", "telefono", "fecha_nacimiento", "calle", "altura", "provincia", "localidad"));
        formJuridica.setCampos(Arrays.asList("razon_social", "rubro", "tipo_empresa", "whatsapp_jur", "telefono_jur", "calle_jur", "altura_jur", "provincia_jur", "localidad_jur"));
        formAdmin.setCampos(Arrays.asList("nombre", "fecha_de_alta"));

        instance.guardarForms(formHumana);
        instance.guardarForms(formJuridica);
        instance.guardarForms(formAdmin);

        /* ---------- LA PRUEBA DEFINITIVA ---------- */

        FormularioCompleto formCompleto1 = new FormularioCompleto();
        formCompleto1.getCamposCompletos().put("nombre", "Juan");
        formCompleto1.getCamposCompletos().put("apellido", "Perez");
        formCompleto1.getCamposCompletos().put("tipo_documento", "DNI");
        formCompleto1.getCamposCompletos().put("numero_documento", "24987236");
        formCompleto1.getCamposCompletos().put("whatsapp", "1122334455");
        formCompleto1.getCamposCompletos().put("telefono", "1122334455");
        formCompleto1.getCamposCompletos().put("fecha_nacimiento", "1975-09-10");
        formCompleto1.getCamposCompletos().put("calle", "Cuenca");
        formCompleto1.getCamposCompletos().put("altura", "3035");
        formCompleto1.getCamposCompletos().put("provincia", "Buenos Aires");
        formCompleto1.getCamposCompletos().put("localidad", "CABA");

        List<TipoDeColaboracion> colaboraciones = new ArrayList<>();
        List<OfertaProductosOServicios> ofertas = new ArrayList<>();
        List<Localidad> zonasQueFrecuenta = new ArrayList<>();
        List<MedioDeContacto> medioDeContactos = new ArrayList<>();
        List<Solicitud> solicitudes = new ArrayList<>();
        List<Suscripcion> suscripciones = new ArrayList<>();
        List<Mensaje> mensajes = new ArrayList<>();
        ColaboradorPublisher colaboradorPublisher = null;
        SolicitudController solicitud = null;
        HeladeraController heladeraController = null;

        Colaborador colaborador3 = new Colaborador(false, formCompleto1, 10.0, "robertito@mail.com", colaboraciones, ofertas, zonasQueFrecuenta, null, medioDeContactos, viandas, solicitudes, suscripciones, mensajes, colaboradorPublisher, solicitud, heladeraController);
        Tarjeta tarjeta2 = new Tarjeta(colaborador3);
        colaborador3.setTarjeta(tarjeta2);
        instance.guardarColaborador(colaborador3);

        DonacionDeViandas colab6 = new DonacionDeViandas(viandas, heladera1, 2, colaborador3);
        DonacionDeViandas colab7 = new DonacionDeViandas(viandas, heladera1, 1, colaborador3);
        DonacionDeViandas colab8 = new DonacionDeViandas(viandas, heladera1, 5, colaborador3);
        DonacionDeViandas colab9 = new DonacionDeViandas(viandas, heladera1, 2, colaborador3);
        DonacionDeViandas colab10 = new DonacionDeViandas(viandas, heladera1, 7, colaborador3);
        DonacionDeViandas colab11 = new DonacionDeViandas(viandas, heladera1, 2, colaborador3);

        instance.guardarColaborador(colaborador3);

        instance.guardarColaboracion(colab6);
        instance.guardarColaboracion(colab7);
        instance.guardarColaboracion(colab8);
        instance.guardarColaboracion(colab9);
        instance.guardarColaboracion(colab10);
        instance.guardarColaboracion(colab11);



        /* ---------- ADMIN ---------- */

        FormularioCompleto formCompletoAdmin = new FormularioCompleto();
        formCompletoAdmin.getCamposCompletos().put("nombre", "Admin");
        formCompletoAdmin.getCamposCompletos().put("fecha_de_alta", "2021-06-15");

        Colaborador colaboradorAdmin = new Colaborador(false, formCompletoAdmin, 0.0, "admin@mail.com", colaboraciones, ofertas, zonasQueFrecuenta, null, medioDeContactos, viandas, solicitudes, suscripciones, mensajes, colaboradorPublisher, solicitud, heladeraController);
        Tarjeta tarjetaAdmin = new Tarjeta(colaborador3);
        colaboradorAdmin.setTarjeta(tarjetaAdmin);

        Usuario usuarioAdmin = new Usuario("admin", "admin", TipoRol.ADMIN, colaboradorAdmin, null);
        instance.guardarUsuario(usuarioAdmin);

        /* ---------- TAREAS PENDIENTES ---------- */

        FallaTecnica fallaTecnica1 = new FallaTecnica(heladera1, colaborador3, "No enfria", "foto1");
        fallaTecnica1.setFecha(LocalDate.now());
        fallaTecnica1.setHora(LocalTime.now());
        TrabajoPendiente trabajoPendiente1 = new TrabajoPendiente(heladera1, fallaTecnica1);

        FallaTecnica fallaTecnica2 = new FallaTecnica(heladera1, colaborador3, "No enfria", "foto1");
        fallaTecnica2.setFecha(LocalDate.now());
        fallaTecnica2.setHora(LocalTime.now());
        TrabajoPendiente trabajoPendiente2 = new TrabajoPendiente(heladera1, fallaTecnica1);

        /* ---------- TECNICO ---------- */

        MedioDeContacto medioDeContactoTecnico = new MedioDeContacto(TipoMedioContacto.MAIL, "tecnico@mail.com");
        Localidad agronomia = new Localidad("Agronomía");
        Localidad urquiza = new Localidad("Villa Urquiza");
        Localidad devoto = new Localidad("Villa Devoto");

        Tecnico tecnico = new Tecnico("Roberto", "Gomez", TipoDocumento.DNI, "25977896", "20-25977896-6", medioDeContactoTecnico);

        tecnico.getAreaDeCobertura().add(agronomia);
        tecnico.getAreaDeCobertura().add(urquiza);
        tecnico.getAreaDeCobertura().add(devoto);
        tecnico.getAreaDeCobertura().add(caba);


        Usuario usuarioTecnico = new Usuario("tecnico", "tecnico", TipoRol.TECNICO, colaborador2, tecnico); // le pongo un colaborador para que no traiga problemas

        tecnico.getTrabajosPendientes().add(trabajoPendiente1);
        tecnico.getTrabajosPendientes().add(trabajoPendiente2);

        instance.guardarUsuario(usuarioTecnico);

        trabajoPendiente1.setTecnico(tecnico);
        trabajoPendiente2.setTecnico(tecnico);

        instance.guardarTecnico(tecnico);

        /* ---------- HUMANO ---------- */

        Usuario usuario1 = new Usuario("humano", "1234", TipoRol.HUMANA, colaborador1, null);
        instance.guardarUsuario(usuario1);

        /* ---------- JURIDICO ---------- */

        Usuario usuario5 = new Usuario("delareina", "Marta.1958", TipoRol.JURIDICA, colaborador5, null);
        instance.guardarUsuario(usuario5);

        /* ---------- SUSCRIPCIONES ---------- */

        Usuario usuario2 = new Usuario("juridico", "1234", TipoRol.JURIDICA, colaborador3, null);
        instance.guardarUsuario(usuario2);

        Suscripcion suscripcion = new Suscripcion(colaborador3, 5, heladera1);
        instance.guardarSuscripcion(suscripcion);

        // ----------------------------------------------- //

        FormularioCompleto formCompleto2 = new FormularioCompleto();
        formCompleto2.getCamposCompletos().put("nombre", "Jorge");
        formCompleto2.getCamposCompletos().put("apellido", "Luran");
        formCompleto2.getCamposCompletos().put("tipo_documento", "DNI");
        formCompleto2.getCamposCompletos().put("numero_documento", "25977896");
        formCompleto2.getCamposCompletos().put("whatsapp", "1154324455");
        formCompleto2.getCamposCompletos().put("telefono", "1154324455");
        formCompleto2.getCamposCompletos().put("fecha_nacimiento", "1999-06-15");
        formCompleto2.getCamposCompletos().put("calle", "Rodriguez");
        formCompleto2.getCamposCompletos().put("altura", "5635");
        formCompleto2.getCamposCompletos().put("provincia", "Buenos Aires");
        formCompleto2.getCamposCompletos().put("localidad", "CABA");

        List<TipoDeColaboracion> colaboraciones2 = new ArrayList<>();
        List<OfertaProductosOServicios> ofertas2 = new ArrayList<>();
        List<Localidad> zonasQueFrecuenta1 = new ArrayList<>();
        List<MedioDeContacto> medioDeContactos1 = new ArrayList<>();
        List<Suscripcion> suscripciones1 = new ArrayList<>();
        List<Solicitud> solicitudes1 = new ArrayList<>();
        List<Mensaje> mensajes1 = new ArrayList<>();

        ColaboradorPublisher colaboradorPublisher1 = null;
        SolicitudController solicitud1 = null;
        HeladeraController heladeraController1 = null;


        Colaborador colaborador4 = new Colaborador(false, formCompleto2, 10.0, "robertote@mail.com", colaboraciones2, ofertas2, zonasQueFrecuenta1, null, medioDeContactos1, viandas, solicitudes1, suscripciones1, mensajes1, colaboradorPublisher1, solicitud1, heladeraController1);
        instance.guardarColaborador(colaborador4);

        Tarjeta tarjeta3 = new Tarjeta(colaborador3);
        colaborador4.setTarjeta(tarjeta3);


        DonacionDeViandas colab12 = new DonacionDeViandas(viandas, heladera1, 2, colaborador4);
        DonacionDeViandas colab13 = new DonacionDeViandas(viandas, heladera1, 2, colaborador4);
        DonacionDeViandas colab14 = new DonacionDeViandas(viandas, heladera1, 1, colaborador3);
        DonacionDeViandas colab15 = new DonacionDeViandas(viandas, heladera1, 5, colaborador3);
        DonacionDeViandas colab16 = new DonacionDeViandas(viandas, heladera1, 2, colaborador3);
        DonacionDeViandas colab17 = new DonacionDeViandas(viandas, heladera1, 1, colaborador3);
        DonacionDeViandas colab18 = new DonacionDeViandas(viandas, heladera1, 5, colaborador3);
        DonacionDeViandas colab19 = new DonacionDeViandas(viandas, heladera1, 2, colaborador3);

        instance.guardarColaborador(colaborador4);

        instance.guardarColaboracion(colab12);
        instance.guardarColaboracion(colab13);
        instance.guardarColaboracion(colab14);
        instance.guardarColaboracion(colab15);
        instance.guardarColaboracion(colab16);
        instance.guardarColaboracion(colab17);
        instance.guardarColaboracion(colab18);
        instance.guardarColaboracion(colab19);


        colaboraciones2.add(colab12);
        colaboraciones2.add(colab13);
        colaboraciones2.add(colab14);
        colaboraciones2.add(colab15);
        colaboraciones2.add(colab16);
        colaboraciones2.add(colab17);
        colaboraciones2.add(colab18);
        colaboraciones2.add(colab19);


        Usuario usuario3 = new Usuario("Fulana", "asd325", TipoRol.HUMANA, colaborador4, null);
        instance.guardarUsuario(usuario3);

    }

    private void guardarColaboracion(Object o) {
        this.colaboracionRepository.guardar(o);
    }

    private void guardarUsuario(Object o) {
        this.usuariosRepository.guardar(o);
    }

    private void guardarForms(Object o) {
        this.formulariosRepository.guardar(o);
    }

    private void guardarColaborador(Colaborador colaborador) {
        this.colaboradorRepository.guardar(colaborador);
    }

    private void guardarSuscripcion(Suscripcion suscripcion) {
        this.suscripcionRepository.guardar(suscripcion);
    }

    private void guardarHeladera(Heladera heladera) {
        this.heladerasRepository.guardar(heladera);
    }

    private void guardarTecnico(Tecnico tecnico) {
        this.tecnicoRepository.guardar(tecnico);
    }
}
