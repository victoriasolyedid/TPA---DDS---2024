package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;

import javax.mail.MessagingException;

public interface AdapterWhatsapp {
    public void enviarMensajeA(Object a, String mensaje, String valor) throws MessagingException;
}
