package ar.edu.utn.frba.dds.factories;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import io.javalin.http.Context;

import java.util.Objects;
import java.util.Optional;

public class SuscripcionFactory {

    public static Suscripcion crear(Context context, HeladerasRepository heladerasRepository) {
        // Validar que el usuario esté presente en el contexto antes de continuar
        Usuario user = context.sessionAttribute("user");
        if (user == null) {
            throw new IllegalStateException("El usuario no está presente en el contexto.");
        }

        // Validar que el topic sea diferente de "DESPERFECTO"
        String topic = context.formParam("suscripcion");
        int cantidadViandas = 0;  // Valor por defecto si el topic es "DESPERFECTO"

        if (!Objects.equals(topic, "DESPERFECTO")) {
            String nvalue = context.formParam("nValue");
            if (nvalue == null) {
                throw new IllegalArgumentException("El parámetro 'nValue' es requerido.");
            }
            try {
                cantidadViandas = Integer.parseInt(nvalue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El valor de 'nValue' no es un número válido", e);
            }
        }

        // Buscar la heladera y validar si está presente
        Optional<Heladera> heladeraOpt = heladerasRepository.buscar(Long.valueOf(context.formParam("heladera")));
        if (heladeraOpt.isEmpty()) {
            throw new IllegalArgumentException("La heladera especificada no existe.");
        }

        // Crear la suscripción con todos los datos necesarios
        Suscripcion suscripcion = new Suscripcion(user.getColaborador(), cantidadViandas, heladeraOpt.get());

        return suscripcion;
    }
}
