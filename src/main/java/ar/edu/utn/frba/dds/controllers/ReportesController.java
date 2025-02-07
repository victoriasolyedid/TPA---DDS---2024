package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportesController {
    public void reporteView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();

        // Obtener la lista de archivos PDF generados
        File folder = new File("src/main/java/ar/edu/utn/frba/dds/reportes/pdfs");
        File[] listOfFiles = folder.listFiles();
        List<String> pdfFiles = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".pdf")) {
                    pdfFiles.add(file.getName());
                }
            }
        }

        // Agregar la lista de archivos PDF al modelo
        model.put("pdfFiles", pdfFiles);

        ctx.render("incidentes/reportes.hbs", model);
    }
}
