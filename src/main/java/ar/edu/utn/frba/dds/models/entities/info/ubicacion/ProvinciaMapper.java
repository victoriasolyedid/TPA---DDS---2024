package ar.edu.utn.frba.dds.models.entities.info.ubicacion;

import java.util.HashMap;
import java.util.Map;

public class ProvinciaMapper {
    private static final Map<String, String> provinceMap = new HashMap<>();

    static {
        provinceMap.put("06", "Buenos Aires");
        provinceMap.put("10", "Catamarca");
        provinceMap.put("22", "Chaco");
        provinceMap.put("26", "Chubut");
        provinceMap.put("02", "Ciudad Autónoma de Buenos Aires");
        provinceMap.put("14", "Córdoba");
        provinceMap.put("18", "Corrientes");
        provinceMap.put("30", "Entre Ríos");
        provinceMap.put("34", "Formosa");
        provinceMap.put("38", "Jujuy");
        provinceMap.put("42", "La Pampa");
        provinceMap.put("46", "La Rioja");
        provinceMap.put("50", "Mendoza");
        provinceMap.put("54", "Misiones");
        provinceMap.put("58", "Neuquén");
        provinceMap.put("62", "Río Negro");
        provinceMap.put("66", "Salta");
        provinceMap.put("70", "San Juan");
        provinceMap.put("74", "San Luis");
        provinceMap.put("78", "Santa Cruz");
        provinceMap.put("82", "Santa Fe");
        provinceMap.put("86", "Santiago del Estero");
        provinceMap.put("94", "Tierra del Fuego");
        provinceMap.put("90", "Tucumán");
    }

    public static String getNombreProvincia(String code) {
        return provinceMap.getOrDefault(code, "Unknown");
    }

    public static String getCodigoProvincia(String nombre) {
        for (Map.Entry<String, String> entry : provinceMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(nombre)) {
                return entry.getKey();
            }
        }
        return "Unknown";
    }
}