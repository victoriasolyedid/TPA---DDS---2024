package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;

import javax.mail.MessagingException;

public interface AdapterTelegram {
    public void enviarMensajeA(Object a, String mensaje, String valor) throws MessagingException;

}
