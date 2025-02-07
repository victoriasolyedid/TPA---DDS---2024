package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.dtos.inputs.MensajeInputDTO;
import ar.edu.utn.frba.dds.factories.SuscripcionFactory;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.*;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.suscripciones.sugeridor.SugeridorDeHeladeras;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.MessagesRepository;
import ar.edu.utn.frba.dds.models.repositories.SuscripcionRepository;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class SuscripcionController implements ICrudViewsHandler {
    private MessagesRepository messagesRepository;
    private SuscripcionRepository suscripcionRepository;
    private HeladerasRepository heladerasRepository;
    private List<Notificador> notificadores;
    private SugeridorDeHeladeras sugeridorDeHeladeras;
    private Map<String, List<Suscripcion>> suscripciones;

    // Constructor simplificado, ahora el broker es inyectado automáticamente
    public SuscripcionController(MessagesRepository messagesRepository, HeladerasRepository heladerasRepository, SuscripcionRepository suscripcionRepository) {
        this.messagesRepository = messagesRepository;
        this.notificadores = List.of(Whatsapp.getInstancia(), Telegram.getInstancia(), Mail.getInstancia());
        this.suscripciones = new HashMap<>();
        this.heladerasRepository = heladerasRepository;
        this.suscripcionRepository = suscripcionRepository;
    }

    // Registrar suscripción a un topic para un colaborador
    public void registrarSuscripcion(Context ctx) {
        Suscripcion suscripcion = SuscripcionFactory.crear(ctx, heladerasRepository);
        Colaborador colaborador = suscripcion.getColaborador();
        Heladera heladera = suscripcion.getHeladera();

        if (colaborador.estaEnZonaQueFrecuenta(heladera)) {
            // TODO: PROBAR ZONAS QUE FRECUENTA suscripcionRepository.guardar(suscripcion);
            System.out.println("Se registro la suscripcion");
        } else {
            System.out.println("El colaborador " + colaborador.getNombre() + " no frecuenta la zona en la que está la heladera");
        }

        ctx.redirect("/nueva-suscripcion");
    }

    // Manejo de mensajes recibidos de los topics
    public void handleMessage(Heladera heladera) throws IOException {

        List<Suscripcion> suscripciones = suscripcionRepository.buscarSuscripcionesPorHeladera(heladera);
        MensajeInputDTO mensajeAEnviar = MensajeInputDTO.builder().
                id(Long.valueOf(RandomStringUtils.randomNumeric(10))).
                build();

        if (suscripciones != null) {

            for (Suscripcion suscripcion : suscripciones) {
                // Verificar las condiciones para cada suscripción
                if (Objects.equals(heladera.getCantViandas(), suscripcion.getCantViandasDeseadas())) {
                    mensajeAEnviar.setContenido("Quedan " + suscripcion.getCantidadViandasDeseadas() + " viandas");
                    this.notificarColaborador(suscripcion.getColaborador(), mensajeAEnviar.getContenido());
                    ;
                } else if (!heladera.getActiva()) {
                    mensajeAEnviar.setContenido("La heladera ha sufrido un desperfecto");
                    this.notificarColaborador(suscripcion.getColaborador(), mensajeAEnviar.getContenido());
                } else if ((heladera.getCapacidad() - heladera.getCantViandas()) <= suscripcion.getCantViandasDeseadas()) {
                    List<Localidad> zonasFrecuentadas = suscripcion.getColaborador().getZonasQueFrecuenta();
                    List<Heladera> heladerasRecomendadas = this.sugeridorDeHeladeras.obtenerHeladeras(zonasFrecuentadas);

                    mensajeAEnviar.setContenido("Heladeras recomendadas: " + heladerasRecomendadas);
                    this.notificarColaborador(suscripcion.getColaborador(), mensajeAEnviar.getContenido());
                }

                messagesRepository.guardar(mensajeAEnviar);
            }

        }
    }

    // Notificación a un colaborador suscripto y persistir mensae\je
    public void notificarColaborador(Colaborador colaborador, String mensaje) {
        if (colaborador != null) {
            List<MedioDeContacto> medios = colaborador.getMedioDeContacto();

            if (medios != null && !medios.isEmpty()) {
                for (MedioDeContacto medio : medios) {
                    if (medio != null && medio.getTipo() != null && medio.getValor() != null) {
                        try {
                            Notificador notificador = notificadores.stream()
                                    .filter(notif -> notif.getTipo().equals(medio.getTipo()))
                                    .findFirst()
                                    .orElse(null);

                            if (notificador != null) {
                                notificador.enviarMensajeA(colaborador, mensaje, medio.getValor());
                                System.out.println("Colaborador notificado a través de " + medio.getTipo());
                            } else {
                                System.err.println("No se encontró notificador para el tipo: " + medio.getTipo());
                            }
                        } catch (MessagingException e) {
                            System.err.println("Error al notificar colaborador: " + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.err.println("Medio de contacto no válido: " + medio);
                    }
                }
            } else {
                System.err.println("El colaborador no tiene medios de contacto válidos.");
            }
        } else {
            System.err.println("El colaborador es null.");
        }
    }

    @Override
    public void index(Context context) {
        Map<String, Object> model = context.sessionAttributeMap();

        List<Heladera> heladeras = this.heladerasRepository.buscarTodos();
        model.put("heladeras", heladeras);

        context.render("suscripciones/nuevaSuscripcion.hbs", model);
    }

    @Override
    public void crear(Context context) {
    }

    @Override
    public void guardar(Context context) {
    }

    @Override
    public void actualizar(Context context) {
    }

    @Override
    public void eliminar(Context context) {
    }
}
