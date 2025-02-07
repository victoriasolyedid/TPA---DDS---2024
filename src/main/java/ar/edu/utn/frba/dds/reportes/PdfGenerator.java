package ar.edu.utn.frba.dds.reportes;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

public class PdfGenerator {
    public <T extends HtmlConvertible> void generarPdf(List<T> elementos, String titulo, List<String> campos, String nombreArchivo) {
        Document document = new Document();
        LocalDate fecha = LocalDate.now();
        String path = "src/main/java/ar/edu/utn/frba/dds/reportes/pdfs/" + nombreArchivo + fecha.toString() + ".pdf";
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<h1>").append(titulo).append("</h1>");
            htmlContent.append("<table style='width:100%; border: 1px solid black;'>");
            htmlContent.append("<tr>");
            for (String campo : campos) {
                htmlContent.append("<th>").append(campo).append("</th>");
            }
            htmlContent.append("</tr>");

            for (T elemento : elementos) {
                htmlContent.append("<tr>");
                htmlContent.append(elemento.toHtml(nombreArchivo));
                htmlContent.append("</tr>");
            }

            htmlContent.append("</table>");

            String toGenerate = htmlContent.toString();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(toGenerate));
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}