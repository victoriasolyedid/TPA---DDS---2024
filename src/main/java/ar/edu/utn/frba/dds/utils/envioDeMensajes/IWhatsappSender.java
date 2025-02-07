package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import lombok.*;

@Setter
public class IWhatsappSender implements AdapterWhatsapp {
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    String numeroDeOrigen = "";

    @Override
    public void enviarMensajeA(Object a, String mensaje, String valor) {
        // Inicializa la cuenta de Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String numeroDeDestino = valor;

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(numeroDeDestino),
                new com.twilio.type.PhoneNumber(numeroDeOrigen),
                mensaje)
                .create();

        System.out.println("El mensaje fue enviado correctamente por WhatsApp a: " + valor);
        System.out.println("El mensaje enviado fue: " + mensaje);
        System.out.println(message.getSid());
    }
}