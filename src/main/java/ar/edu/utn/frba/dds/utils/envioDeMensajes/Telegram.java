package ar.edu.utn.frba.dds.models.entities.envioDeMensajes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;

import javax.mail.MessagingException;


@Getter
public class Telegram extends Notificador {

    private TipoMedioContacto tipo = TipoMedioContacto.TELEGRAM;
    private AdapterTelegram adapter;
    private static Telegram instancia = null;

    // Constructor privado para evitar instancias externas
    public Telegram(AdapterTelegram adapter) {
        this.adapter = adapter;
    }

    // Método getInstancia corregido
    public static Telegram getInstancia() {
        if (instancia == null) {
            instancia = new Telegram(new AdapterTelegram() {
                @Override
                public void enviarMensajeA(Object a, String mensaje, String valor) throws MessagingException {

                }
            }); // Crea el AdapterTelegram si es necesario
        }
        return instancia;
    }

    @Override
    public void enviarMensajeA(Object a, String texto, String valor) throws MessagingException {
        adapter.enviarMensajeA(a, texto, valor);
    }
}
