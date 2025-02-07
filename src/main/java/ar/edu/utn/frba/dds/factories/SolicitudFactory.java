package ar.edu.utn.frba.dds.factories;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Solicitud;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.Optional;

public class SolicitudFactory {

    public static Solicitud crear(Context context, Optional<Heladera> heladera) {

        Solicitud solicitud;
        Optional<Colaborador> colab = null;
        TipoDeColaboracion tipoColab;

        Usuario user = context.sessionAttribute("user");

        if(user != null) {
            colab = ServiceLocator.instanceOf(ColaboradorRepository.class).buscar(user.getColaborador().getId());
        }

        if(context.sessionAttribute("motivo") == "Donar Vianda/s") {
            tipoColab = new DonacionDeViandas();
        } else {
            tipoColab = new HacerseCargoDeHeladera();
        }

        solicitud = Solicitud.builder()
                .colaboracion(tipoColab)
                .heladera(heladera.get())
                .fechaYHora(LocalDateTime.now())
                .build();

        if(colab.isPresent()){
            solicitud.setColaborador(colab.get());
        }

        return solicitud;


    }
}
