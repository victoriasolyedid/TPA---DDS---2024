package ar.edu.utn.frba.dds.models.entities.sistemaDePuntos;

import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;

import java.util.List;

public class SistemaDePuntos {
    private Colaborador colaborador;
    public void calcularPuntaje(){
       var puntajeTotal = colaborador.getColaboraciones().stream()
                .filter(colaboracion -> !(colaboracion instanceof HacerseCargoDeHeladera))
                .mapToDouble(TipoDeColaboracion::puntajeAsociado)
                .sum() + calcularPuntajeHeladera() - colaborador.puntosCanjeados();
       colaborador.setPuntosAcumulados(puntajeTotal);
    }
    public double calcularPuntajeHeladera(){
        return this.cantidadDeHeladerasActivas() * this.puntajeParcialHeladeras();
    }
    public double puntajeParcialHeladeras(){
        return this.heladerasActivas().stream()
                .mapToDouble(TipoDeColaboracion::puntajeAsociado)
                .sum();
    }
    public  double cantidadDeHeladerasActivas(){

        return this.heladerasActivas().size();
    }
    public List<TipoDeColaboracion> heladerasActivas(){
        return   colaborador.getColaboraciones().stream()
                .filter(colaboracion -> colaboracion instanceof HacerseCargoDeHeladera)
                .filter(colaboracion -> ((HacerseCargoDeHeladera) colaboracion).getHeladera().getActiva()).toList();
    }
}
