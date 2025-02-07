package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Mensaje;
import ar.edu.utn.frba.dds.models.repositories.MessagesRepository;
import org.apache.commons.lang3.RandomStringUtils;

import javax.transaction.Transactional;

public class MainMensajes {
    private MessagesRepository messagesRepositorio;

    public static void main(String[] args) {
        MainMensajes instance = new MainMensajes();
        // instance.messagesRepositorio = MessagesRepository.getInstancia();

        instance.guardarMensaje();
    }

    @Transactional
    public void guardarMensaje() {
        Colaborador colab = new Colaborador("juan123@gmail.com", false);
        Long numericId = Long.parseLong(RandomStringUtils.randomNumeric(10));
        Mensaje mensaje1 = new Mensaje(numericId, colab, "contenido del mensaje");

        this.messagesRepositorio.guardar(mensaje1);
    }
}
