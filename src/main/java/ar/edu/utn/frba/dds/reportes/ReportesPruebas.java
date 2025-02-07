package ar.edu.utn.frba.dds.reportes;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.ColaboracionController;
import ar.edu.utn.frba.dds.controllers.HeladeraController;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DistribucionDeVianda;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.MotivoDistribucion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.controllers.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.incidentes.Alerta;
import ar.edu.utn.frba.dds.models.entities.incidentes.FallaTecnica;
import ar.edu.utn.frba.dds.models.entities.incidentes.TipoAlerta;
import io.javalin.http.Context;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReportesPruebas {
    public static void main(String[] args) throws IOException {
        IncidenteController incidenteController = ServiceLocator.instanceOf(IncidenteController.class);
        HeladeraController heladeraController = ServiceLocator.instanceOf(HeladeraController.class);
        ColaboracionController colaboracionController = ServiceLocator.instanceOf(ColaboracionController.class);

        Colaborador colaborador1 = new Colaborador("juan.perez@example.com", false);
        Colaborador colaborador2 = new Colaborador("margarita.roldan@example.com", false);



        // ************ COLABORACIONES ************
        Coordenadas coordenadas = new Coordenadas(-34.603722, -58.381592);
        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");
        Ubicacion ubicacionMedrano = new Ubicacion("Buenos Aires", "2023", buenosAires, caba);
        Ubicacion ubicacionCampus = new Ubicacion("Buenos Aires", "2162", buenosAires, caba);

        LocalDateTime fechaHoraActual = LocalDateTime.now();
        ModeloHeladera temps = new ModeloHeladera("ChillBro", 0.0, 20.0);

        // Crear heladeras
        Heladera heladera1 = crearHeladera("Medrano", ubicacionMedrano, temps);
        Heladera heladera2 = crearHeladera("Campus", ubicacionCampus, temps);

        heladeraController.crear((Context) heladera1);
        heladeraController.crear((Context) heladera2);

        Vianda vianda1 = new Vianda("Milanesa");
        Vianda vianda2 = new Vianda("Ensalada");
        Vianda vianda3 = new Vianda("Pollo");
        Vianda vianda4 = new Vianda("Papas");
        Vianda vianda5 = new Vianda("Arroz");

        List<Vianda> viandas1 = List.of(vianda5, vianda2, vianda3, vianda4);
        List<Vianda> viandas2 = List.of(vianda1, vianda3, vianda4, vianda5);
        List<Vianda> viandas3 = List.of(vianda4, vianda2, vianda5);

        DonacionDeViandas colaboracion1 = new DonacionDeViandas(viandas1, heladera1, 10, colaborador1);
        DonacionDeViandas colaboracion2 = new DonacionDeViandas(viandas3, heladera2, 24, colaborador2);
        DistribucionDeVianda colaboracion3 = new DistribucionDeVianda(heladera1, heladera2, 4, MotivoDistribucion.FALTA_DE_VIANDAS, colaborador1);
        DistribucionDeVianda colaboracion4 = new DistribucionDeVianda(heladera2, heladera1, 45, MotivoDistribucion.FALTA_DE_VIANDAS, colaborador2);
        DistribucionDeVianda colaboracion5 = new DistribucionDeVianda(heladera1, heladera2, 2, MotivoDistribucion.FALTA_DE_VIANDAS, colaborador1);

        colaboracionController.guardar((Context) colaboracion1);
        colaboracionController.guardar((Context) colaboracion2);
        colaboracionController.guardar((Context) colaboracion3);
        colaboracionController.guardar((Context) colaboracion4);
        colaboracionController.guardar((Context) colaboracion5);

        // ************ INCIDENTES ************
        Alerta incidente1 = new Alerta(heladera1, TipoAlerta.FRAUDE);
        FallaTecnica incidente2 = new FallaTecnica(heladera1, colaborador1, "Falla en el servidor", "foto.jpg");
        Alerta incidente3 = new Alerta(heladera1, TipoAlerta.TEMPERATURA);
        FallaTecnica incidente4 = new FallaTecnica(heladera2, colaborador2, "Se ha rompido", "foto.jpg");
        Alerta incidente5 = new Alerta(heladera2, TipoAlerta.FALLA_CONEXION);

        LocalTime now = LocalTime.now();
        LocalDate oneMonthAgo = LocalDate.ofEpochDay(1);
        incidente1.setFecha(oneMonthAgo);
        incidente1.setHora(now);

        incidenteController.crear((Context) incidente1, TipoIncidente.ALERTA);
        incidenteController.crear((Context) incidente4, TipoIncidente.FALLA_TECNICA);
        incidenteController.crear((Context) incidente3, TipoIncidente.ALERTA);
        incidenteController.crear((Context) incidente2, TipoIncidente.FALLA_TECNICA);
        incidenteController.crear((Context) incidente5, TipoIncidente.ALERTA);

        // ************ REPORTES ************
        Reportes generadorReportes = Reportes.getInstancia();
        generadorReportes.generarReporteFallasPorHeladera();
        generadorReportes.generarReporteViandasPorHeladera();
        generadorReportes.generarReporteViandasPorColaborador();
    }

    private static Heladera crearHeladera(String nombre, Ubicacion ubicacion, ModeloHeladera modeloHeladera) {
        // Puedes ajustar estos parámetros según sea necesario
        int capacidad = 30; // Ejemplo de capacidad
        LocalDateTime fechaDeInstalacion = LocalDateTime.now(); // Ejemplo de fecha de instalación
        return new Heladera(ubicacion, capacidad, fechaDeInstalacion, modeloHeladera, nombre);
    }
}
