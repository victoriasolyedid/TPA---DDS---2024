package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Coordenadas;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.repositories.ColaboracionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainColaboracion {

    private ColaboracionRepository colaboracionRepositorio;

    public static void main (String[] args){
        MainColaboracion instance = new MainColaboracion();
        instance.colaboracionRepositorio = ServiceLocator.instanceOf(ColaboracionRepository.class);
        instance.guardarColaboracion();
    }

    private void guardarColaboracion() {
        Vianda vianda1 = new Vianda("milanesa con pure");
        List<Vianda> viandas = new ArrayList<>();
        viandas.add(vianda1);

        Coordenadas coordenadas = new Coordenadas(-34.603722, -58.381592);
        Provincia buenosAires = new Provincia("Buenos Aires");
        Localidad caba = new Localidad("CABA");
        Ubicacion ubicacion1 = new Ubicacion("Buenos Aires", "2023", buenosAires, caba);

        ModeloHeladera modeloHeladera = new ModeloHeladera("CoolMaster", -5.0, 5.0);

        Colaborador colaborador1 = new Colaborador("jose@gmail.com",false);

        Heladera heladera1 = new Heladera(ubicacion1,50,LocalDateTime.now(),modeloHeladera,"Heladera 1");

        DonacionDeViandas colab1 = new DonacionDeViandas(viandas,heladera1,5,colaborador1);

        this.colaboracionRepositorio.guardar(colab1);
    }
}
