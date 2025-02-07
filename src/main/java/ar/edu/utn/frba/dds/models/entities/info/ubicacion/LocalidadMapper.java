package ar.edu.utn.frba.dds.models.entities.info.ubicacion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LocalidadMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getNombreLocalidad(String localidadId) {
        if ("Seleccionar Localidad".equals(localidadId)) {
            System.err.println("No se seleccionó ninguna localidad");
            return null;
        }

        String urlLocalidades = "https://apis.datos.gob.ar/georef/api/localidades?id=" + localidadId + "&aplanar=true&exacto=true";
        String urlMunicipios = "https://apis.datos.gob.ar/georef/api/municipios?id=" + localidadId + "&aplanar=true&exacto=true";

        String localidad = fetchLocalidad(urlLocalidades);
        if (localidad == null) {
            localidad = fetchMunicipio(urlMunicipios);
        }

        return localidad != null ? localidad : "Desconocida";
    }

    private static String fetchLocalidad(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error al buscar la localidad: " + responseCode);
                return null;
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            JsonNode data = objectMapper.readTree(inline.toString());
            JsonNode localidades = data.path("localidades");
            if (localidades.isArray() && !localidades.isEmpty()) {
                return localidades.get(0).path("nombre").asText();
            }

        } catch (IOException e) {
            System.err.println("Error al buscar la localidad: " + e.getMessage());
        }

        return null;
    }

    private static String fetchMunicipio(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Error al buscar el municipio: " + responseCode);
                return null;
            }

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            JsonNode data = objectMapper.readTree(inline.toString());
            JsonNode municipios = data.path("municipios");
            if (municipios.isArray() && !municipios.isEmpty()) {
                return municipios.get(0).path("nombre").asText();
            }

        } catch (IOException e) {
            System.err.println("Error al buscar el municipio: " + e.getMessage());
        }

        return null;
    }
}