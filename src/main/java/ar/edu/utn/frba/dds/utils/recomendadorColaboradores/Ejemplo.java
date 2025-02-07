package ar.edu.utn.frba.dds.models.entities.RecomendadorColaboradores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import java.util.List;

public class Ejemplo {
    public static void main(String[] args) throws Exception {

        RestColaboradoresRecomendadosImpl restColaboradoresRecomendadosImpl = RestColaboradoresRecomendadosImpl.getInstance();
        List<Colaborador> colaboradores = restColaboradoresRecomendadosImpl.createColaboradores();

    }
}
