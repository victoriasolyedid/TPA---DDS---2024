package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;

import javax.mail.MessagingException;

@Getter
public class Whatsapp extends Notificador {

    private AdapterWhatsapp adapter;
    private TipoMedioContacto tipo = TipoMedioContacto.WHATSAPP;
    private static Whatsapp instancia = null;

    // Constructor privado para evitar instancias externas
    public Whatsapp(AdapterWhatsapp adapter) {
        this.adapter = adapter;
    }

    // Método getInstancia corregido
    public static Whatsapp getInstancia() {
        if (instancia == null) {
            instancia = new Whatsapp(new AdapterWhatsapp() {
                @Override
                public void enviarMensajeA(Object a, String mensaje, String valor) throws MessagingException {

                }
            }); // Crea el AdapterWhatsapp si es necesario
        }
        return instancia;
    }

    @Override
    public void enviarMensajeA(Object a, String texto, String valor) throws MessagingException {
        adapter.enviarMensajeA(a, texto, valor);
    }
}
