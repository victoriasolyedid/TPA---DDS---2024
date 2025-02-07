package ar.edu.utn.frba.dds.dtos.outputs;

import ar.edu.utn.frba.dds.dtos.inputs.HeladeraDTO;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;

public class TransformarHeladerasDTO {

    private static TransformarHeladerasDTO instancia = null;
    public static TransformarHeladerasDTO getInstancia() {
        if (instancia == null) {
            instancia = new TransformarHeladerasDTO();
        }
        return instancia;
    }

    public static HeladeraDTO convertirEntidadADTO(Heladera heladera) {
        return new HeladeraDTO(
                //heladera.getID(),
                heladera.getUbicacion(),
                heladera.getCapacidad(),
                heladera.getFechaFuncionamiento().atStartOfDay(),
                heladera.getSensorDeTemperatura(),
                heladera.getSensorDeMovimiento(),
                heladera.getActiva(),
                heladera.getModelo(),
                heladera.getTemperaturaActual() != null ? heladera.getTemperaturaActual() : 0.0,
                heladera.getNombre(),
                heladera.getCantViandas()
        );
    }

//    public static Heladera convertirDTOaEntidad(HeladeraDTO heladeraDTO) {
//        Heladera heladera = new Heladera(
//                heladeraDTO.getUbicacion(),
//                heladeraDTO.getCapacidad(),
//                heladeraDTO.getFechaFuncionamiento(),
//                // heladeraDTO.getRangoTemperatura(),
//                heladeraDTO.getNombre()
//        );
//
//        //heladera.setID(heladeraDTO.getId());
//        heladera.setSensorDeMovimiento(heladeraDTO.getSensorDeMovimiento());
//        heladera.setSensorDeTemperatura(heladeraDTO.getSensorDeTemperatura());
//        heladera.setActiva(heladeraDTO.getActiva());
//        heladera.setTemperaturaActual(heladeraDTO.getTemperaturaActual());
//        heladera.setCantViandas(heladeraDTO.getCantViandas());
//
//        return heladera;
//    }
}
