package ar.edu.utn.frba.dds.models.entities.info.ubicacion;

import ar.edu.utn.frba.dds.models.repositories.Persistente;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter @Setter @Entity @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name="ubicacion")
public class Ubicacion extends Persistente {

    @Embedded
    private Coordenadas coordenadas;

    @Column(name="calle", columnDefinition = "VARCHAR(50)")
    private String calle;

    @Column(name="altura", columnDefinition = "VARCHAR(50)")
    private String altura;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="provincias", referencedColumnName ="id")
    private Provincia provincia;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="localidades", referencedColumnName ="id")
    private Localidad localidad;

    public Ubicacion(String calle, String altura, Provincia provincia, Localidad localidad) {
        this.calle = calle;
        this.altura = altura;
        this.provincia = provincia;
        this.localidad = localidad;
    }


    public void buscarCoordenadas() {
        // TODO Implementar logica
        this.coordenadas = new Coordenadas(123.456, 78.910);
   }

    /*public void buscarCoordenadas() {
        String address = String.format("%s %d, %s, %s", calle, altura, localidad.getLocalidad(), provincia.getProvincia());
        String url = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1", address.replace(" ", "%20"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.body() != null && !response.body().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                if (root.isArray() && !root.isEmpty()) {
                    JsonNode location = root.get(0);
                    double lat = location.path("lat").asDouble();
                    double lon = location.path("lon").asDouble();
                    this.coordenadas = new Coordenadas((float) lat, (float) lon);
                } else {
                    System.out.println("No se encontraron coordenadas para la dirección proporcionada.");
                }
            } else {
                System.out.println("Respuesta vacía del servidor.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error al buscar coordenadas: " + e.getMessage());
        }
    }

    public String ubicacionCompleta(){
        return (calle+ ", " + altura + ", " + provincia + ", " + localidad);
    }*/
}
