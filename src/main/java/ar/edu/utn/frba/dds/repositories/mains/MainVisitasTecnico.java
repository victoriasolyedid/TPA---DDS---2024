package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.models.repositories.VisitasTecnicoRepository;

public class MainVisitasTecnico {

    private VisitasTecnicoRepository visitasTecnicoRepositorio;

    public static void main(String[] args) {
        MainVisitasTecnico instance = new MainVisitasTecnico();

        instance.guardarVisita();
    }

    private void guardarVisita() {
        // TODO
    }
}