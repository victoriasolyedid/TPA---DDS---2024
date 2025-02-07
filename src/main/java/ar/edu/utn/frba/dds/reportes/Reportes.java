package ar.edu.utn.frba.dds.reportes;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.ColaboracionController;
import ar.edu.utn.frba.dds.controllers.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DistribucionDeVianda;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.incidentes.Incidente;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Reportes {
    private static Reportes instancia = null;

    public static Reportes getInstancia() {
        if (instancia == null) {
            instancia = new Reportes();
        }
        return instancia;
    }

    private PdfGenerator pdfGenerator = new PdfGenerator();

    public void generarReporteFallasPorHeladera() {
        IncidenteController incidenteController = ServiceLocator.instanceOf(IncidenteController.class);

        List<Incidente> incidentes = incidenteController.buscarUltimaSemana().stream()
                .sorted(Comparator.comparing(incidente -> incidente.getHeladera() != null ? incidente.getHeladera().getNombre() : ""))
                .toList();

        if (incidentes.isEmpty()) {
            System.out.println("No hay incidentes para generar el reporte.");
            return;
        }

        List<String> campos = List.of("Heladera", "Tipo de falla", "Fecha y hora", "Resuelto");
        this.pdfGenerator.generarPdf(incidentes, "Reporte de fallas por heladera", campos, "Incidentes");
    }


    public void generarReporteViandasPorHeladera() {
        ColaboracionController colaboracionController = ServiceLocator.instanceOf(ColaboracionController.class);
        List<String> campos = List.of("Heladera", "Tipo de Colaboracion", "Cantidad de Viandas", "Colaborador", "Fecha y hora");

        List<TipoDeColaboracion> colaboraciones = colaboracionController.buscarUltimaSemana()
                .stream()
                .filter(colab -> colab instanceof DonacionDeViandas || colab instanceof DistribucionDeVianda)
                .sorted(Comparator.comparing(colab -> {
                    if (colab instanceof DistribucionDeVianda) {
                        return ((DistribucionDeVianda) colab).getHeladeraOrigen().getNombre();
                    } else {
                        return ((DonacionDeViandas) colab).getHeladera().getNombre();
                    }
                }).thenComparing(colab -> ((TipoDeColaboracion) colab).getFechaColaboracion())) // Cast explícito
                .collect(Collectors.toList());


        if (colaboraciones.isEmpty()) {
            System.out.println("No hay colaboraciones para generar el reporte.");
            return; // Retorna si no hay colaboraciones
        }

        this.pdfGenerator.generarPdf(colaboraciones, "Reporte de viandas por heladera", campos, "ViandasPorHeladera");
    }

    public void generarReporteViandasPorColaborador() {
        ColaboracionController colaboracionController = ServiceLocator.instanceOf(ColaboracionController.class);
        List<TipoDeColaboracion> colaboraciones = colaboracionController.buscarTodos()
                .stream()
                .filter(colab -> colab instanceof DonacionDeViandas || colab instanceof DistribucionDeVianda)
                .sorted(Comparator.comparing(colab -> colab.getColaborador().getNombre()))
                .collect(Collectors.toList());

        if (colaboraciones.isEmpty()) {
            System.out.println("No hay colaboraciones para generar el reporte.");
            return; // Retorna si no hay colaboraciones
        }

        List<String> campos = List.of("Colaborador", "Viandas Ingresadas", "Viandas Distribuidas", "Fecha y hora");
        this.pdfGenerator.generarPdf(colaboraciones, "Reporte de viandas por colaborador", campos, "ViandasPorColaborador");
    }

    public static void main(String[] args) {
        try {
            // ---- Cron ---- //
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail job = JobBuilder.newJob(ReportesJob.class)
                    .withIdentity("Reportereso", "Reportes")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("ReporteTrigger", "ReportesTriggers")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            //.withIntervalInHours(168) // Cada una semana
                            .withIntervalInSeconds(20)  // Cada 20 segundos para pruebas
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el stack trace para más detalles sobre la excepción
        }
    }
}
