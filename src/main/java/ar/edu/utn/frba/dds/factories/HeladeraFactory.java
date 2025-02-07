package ar.edu.utn.frba.dds.factories;

import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.SensorDeTemperatura;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.*;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class HeladeraFactory {

    public static Heladera crear(Context context) {
        Heladera heladera;

        System.out.println("---- Usuario heladera: " + context.attribute("userData"));
        Usuario user = context.sessionAttribute("user");

        if (user == null || user.getColaborador() == null) {
            throw new IllegalArgumentException("El colaborador no está disponible.");
        }

        String provinciaCode = context.formParam("provincia");
        Provincia provincia = new Provincia(ProvinciaMapper.getNombreProvincia(provinciaCode));
        String localidadCode = context.formParam("localidad");
        Localidad localidad = new Localidad(LocalidadMapper.getNombreLocalidad(localidadCode));

        Ubicacion ubicacion = new Ubicacion(
                context.formParam("calle"),
                (context.formParam("altura")),
                provincia,
                localidad
        );

        ModeloHeladera modeloHeladera = new ModeloHeladera("FreezeMax 500",-20.00,10.00);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        heladera = Heladera.builder()
                .nombre(context.formParam("nombre"))
                .ubicacion(ubicacion)
                .fechaFuncionamiento(LocalDate.parse(Objects.requireNonNull(context.formParam("fechaDeInicioDeFuncionamiento")), formatter))
                .capacidad(Integer.valueOf(Objects.requireNonNull(context.formParam("capacidad"))))
                .activa(true)
                .cantViandas(0)
                .modelo(modeloHeladera)
                .build();
        SensorDeTemperatura sensorDeTemperatura = new SensorDeTemperatura(heladera);
        SensorDeMovimiento sensorDeMovimiento = new SensorDeMovimiento(heladera);

        heladera.setSensorDeTemperatura(sensorDeTemperatura);
        heladera.setSensorDeMovimiento(sensorDeMovimiento);

        return heladera;
    }
}
