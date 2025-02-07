package ar.edu.utn.frba.dds.reportes;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReportesJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Reportes reportes = Reportes.getInstancia();

        try {
            reportes.generarReporteFallasPorHeladera();
            reportes.generarReporteViandasPorHeladera();
            reportes.generarReporteViandasPorColaborador();
            System.out.println("Reportes generados exitosamente.");
        }
        catch (Exception e) {
            System.out.println("Error al generar reporte: " + e.getMessage());
        }
    }
}
