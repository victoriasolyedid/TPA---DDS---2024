package ar.edu.utn.frba.dds.factories;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.incidentes.Alerta;
import ar.edu.utn.frba.dds.models.entities.incidentes.FallaTecnica;
import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera; // Asegúrate de importar Heladera
import ar.edu.utn.frba.dds.models.repositories.ColaboracionRepository;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class IncidenteFactory {

    private static HeladerasRepository heladeraRepository = ServiceLocator.instanceOf(HeladerasRepository.class);
    private static ColaboradorRepository colaboradorRepository = ServiceLocator.instanceOf(ColaboradorRepository.class);


    public static Incidente crear(Context context, TipoIncidente tipoIncidente) {
        Incidente incidente;
        Usuario user = context.sessionAttribute("user");
        assert user != null;
        Optional<Colaborador> colaborador = colaboradorRepository.buscar(user.getColaborador().getId());

        if (user.getColaborador() == null) {
            throw new IllegalArgumentException("El colaborador no está disponible.");
        }

        // Creamos el incidente según el tipo
        if (tipoIncidente.equals(TipoIncidente.FALLA_TECNICA)) {
            incidente = FallaTecnica.builder()
                    .descripcion(context.formParam("descripcion"))
                    .colaborador(colaborador.get())// Pásalo como String directo
                    .foto(context.formParam("foto"))
                    .build();
            }
            else {
            incidente = new Alerta();
        }

        String idHeladera = context.formParam("heladera");
        Optional<Heladera> heladeraOpt = heladeraRepository.buscar(Long.valueOf(idHeladera));
        if (heladeraOpt.isPresent()) {
            incidente.setHeladera(heladeraOpt.get());
        } else {
            throw new IllegalArgumentException("Heladera no encontrada: " + heladeraOpt);
        }

        // Setear fecha y hora del incidente
        try {
            incidente.setFecha(LocalDate.parse(Objects.requireNonNull(context.formParam("fecha"))));
            incidente.setHora(LocalTime.parse(Objects.requireNonNull(context.formParam("hora"))));
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha u hora inválida: " + e.getMessage());
        }

        incidente.setTipoIncidente(tipoIncidente);
        incidente.setResuelto(false);

        return incidente;
    }

    public static Incidente crearAlerta(Alerta alerta) {
        alerta.setTipoIncidente(TipoIncidente.ALERTA);
        alerta.setResuelto(false);
        alerta.setFecha(LocalDate.now());  // O puedes establecerla según lo necesites
        alerta.setHora(LocalTime.now());   // Lo mismo para la hora

        return alerta;
    }
}
