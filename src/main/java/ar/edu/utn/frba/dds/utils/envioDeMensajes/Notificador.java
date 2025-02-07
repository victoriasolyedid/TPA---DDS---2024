package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;

import javax.mail.MessagingException;

@Getter
public abstract class Notificador {

    private TipoMedioContacto tipo;
    public void enviarMensajeA(Object a, String mensaje, String valor) throws MessagingException{};
}
