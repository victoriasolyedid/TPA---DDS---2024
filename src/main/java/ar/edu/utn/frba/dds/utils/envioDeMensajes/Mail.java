package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;

import javax.mail.MessagingException;

import ar.edu.utn.frba.dds.models.entities.tecnicos.Tecnico;
import lombok.Getter;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

@Getter
public class Mail extends Notificador {

    private TipoMedioContacto tipo = TipoMedioContacto.MAIL;
    private static Mail instancia = null;

    // Configuración SMTP
    private final String smtpServer = "smtp.gmail.com";
    private final Integer smtpPort = 587; // Puerto SMTP (STARTTLS)
    private final String smtpUsername = "SistemaNotificador00@gmail.com";
    private final String smtpPassword = "uemu xhgu mqlz hrdo";

    // Constructor privado para el patrón Singleton
    public Mail() {}

    // Método getInstancia corregido
    public static Mail getInstancia() {
        if (instancia == null) {
            instancia = new Mail();
        }
        return instancia;
    }

    @Override
    public void enviarMensajeA(Object a, String mensaje, String valor) throws MessagingException {
        try {
            if (a instanceof Tecnico) {
                Tecnico tecnico = (Tecnico) a; // Realizar el casting
                HtmlEmail email = new HtmlEmail();
                email.setHostName(smtpServer);
                email.setSmtpPort(smtpPort);
                email.setAuthenticator(new DefaultAuthenticator(smtpUsername, smtpPassword));
                email.setStartTLSEnabled(true);
                email.setFrom(smtpUsername, "Sistema Notificador");
                email.addTo(valor, tecnico.getNombre());
                email.setSubject("Mensaje importante");
                email.setHtmlMsg("<html><body><p>" + mensaje + "</p></body></html>");
                email.send();

                System.out.println("Correo electrónico enviado correctamente");
            } else {
                throw new IllegalArgumentException("El objeto no es un Colaborador");
            }
        } catch (EmailException e) {
            System.err.println("Error al enviar correo electrónico: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
