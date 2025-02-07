package ar.edu.utn.frba.dds.factories;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.OfertaProductosOServicios;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.Rubro;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public class ProductoOServicioFactory {

    private static ColaboradorRepository colaboradorRepository = ServiceLocator.instanceOf(ColaboradorRepository.class);
    private static UsuariosRepository usuariosRepository = ServiceLocator.instanceOf(UsuariosRepository.class);

    public static OfertaProductosOServicios crear(Context context) {
        // Obtener el usuario autenticado desde el contexto
        Usuario user = context.sessionAttribute("user");
        Objects.requireNonNull(user, "El usuario no puede ser nulo.");

        Optional<Colaborador> colaborador = colaboradorRepository.buscar(user.getColaborador().getId());

        // Obtener parámetros del formulario
        String nombre = context.formParam("nombre");
        String rubroStr = context.formParam("rubro");
        String valorEnPuntosStr = context.formParam("valorEnPuntos");
        UploadedFile foto = context.uploadedFile("foto");

        // Validar parámetros
        Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
        Objects.requireNonNull(rubroStr, "El rubro no puede ser nulo.");
        Objects.requireNonNull(valorEnPuntosStr, "El valor en puntos no puede ser nulo.");
        Objects.requireNonNull(foto, "La foto no puede ser nula.");

        // Convertir valorEnPuntos a entero
        int valorEnPuntos;
        try {
            valorEnPuntos = Integer.parseInt(valorEnPuntosStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor en puntos debe ser un número válido.", e);
        }

        // Crear la entidad
        OfertaProductosOServicios productoOServicio = OfertaProductosOServicios.builder()
                .nombre(nombre)
                .rubro(stringARubro(rubroStr))
                .valorEnPuntos(valorEnPuntos)
                .imagen(pasarABase64(foto))
                .build();


        productoOServicio.setColaborador(colaborador.get());
        productoOServicio.setFechaColaboracion(LocalDateTime.now());


        return productoOServicio;
    }

    // Método para convertir el String en Rubro
    public static Rubro stringARubro(String rubro) {
        return new Rubro(rubro);
    }


    public static String pasarABase64(UploadedFile file) {
        String fotoBase64 = null;

        if (file != null) {
            try (InputStream inputStream = file.content()) {
                fotoBase64 = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fotoBase64;
    }
}