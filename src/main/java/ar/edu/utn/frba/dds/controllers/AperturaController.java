package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.heladeras.solicitudesDeApertura.Apertura;
import ar.edu.utn.frba.dds.models.repositories.AperturasRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class AperturaController {
    private AperturasRepository aperturasRepository;

//    public void guardar(Context context, Apertura apertura) {
//        try {
//            Long idHeladera = Long.valueOf(context.pathParam("id"));
//
//            Optional<Heladera> heladera = ServiceLocator.instanceOf(HeladerasRepository.class).buscar(idHeladera);
//            if (heladera.isEmpty()) {
//                context.status(HttpStatus.NOT_FOUND);
//                context.redirect("/404");
//                return;
//            }
//            apertura.setHeladera(heladera.get());
//
//            this.aperturasRepository.guardar(apertura);
//            context.redirect("/heladeras?creada=1"); // TODO: Revisar
//        } catch (Exception e) {
//            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
//            context.redirect("/heladeras?error=1"); // TODO: Revisar
//        }
//    }

    public void guardar(Context ctx) {

    }

    public void eliminar(Context context) {
        Optional<Apertura> apertura = this.aperturasRepository.buscar(Long.parseLong(context.pathParam("id")));
        if (apertura.isEmpty()) {
            context.status(HttpStatus.NOT_FOUND);
            context.redirect("/404");
            return;
        }
        this.aperturasRepository.eliminar(apertura.get());
    }
}
