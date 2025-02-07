package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.OfertaProductosOServicios;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ColaboradorController {
    private ColaboradorRepository colaboradorRepository;

    // * {X} Revisar
    public Colaborador guardar(Context context) {
        Colaborador colaborador = new Colaborador();

        colaborador.setMail(context.formParam("email"));

        if (context.formParam("tipo_usuario").equals("humana")) {
            colaborador.setEsJuridica(false);
        } else if (context.formParam("tipo_usuario").equals("juridica")) {
            colaborador.setEsJuridica(true);
        }

        System.out.println("Va a crear el formulario");
        FormularioCompleto form = ServiceLocator.instanceOf(FormularioCompletoController.class).guardar(context);

        System.out.println("Setea los datos del colaborador");
        colaborador.setDatosColaborador(form);

        System.out.println("Va a guardar al colaborador");
        colaboradorRepository.guardar(colaborador);

        System.out.println("Guardo al colaborador");
        return colaborador;
    }

    // ! { } Revisar | Necesitamos que modificar usuario de usuarioController esté hecha
    public void actualizar(Context context) {
        Colaborador colaborador = context.attribute("colaborador");
        colaboradorRepository.actualizar(colaborador);
    }

    public void actualizar(Colaborador colaborador) {
        colaboradorRepository.actualizar(colaborador);
    }

    // * {X} Revisar
    public void eliminar(Context context) {
        String colaboradorId = context.pathParam("id");

        Optional<Colaborador> colaborador = colaboradorRepository.buscar(Long.valueOf(colaboradorId));
        if (colaborador.isEmpty()) {
            context.status(HttpStatus.NOT_FOUND);
            context.redirect("/404");
            return;
        }
        colaboradorRepository.eliminar(colaborador.get());
    }

    // buscar por mail en el repositorio
    public Optional<Colaborador> buscarPorMail(String mail) {
        return colaboradorRepository.buscarPorMail(mail);
    }

    /*public Optional<Colaborador> buscarPorDocumento(String tipoDoc, String nroDoc) {
        return colaboradorRepository.buscarPorDocumento(tipoDoc, nroDoc);
    }*/

    public boolean realizaCompra(Colaborador colaborador, Optional<OfertaProductosOServicios> prodOServ) {
        if (prodOServ.isPresent()) {
            OfertaProductosOServicios producto = prodOServ.get();
            Double puntosColab = colaborador.getPuntosAcumulados();

            if (producto.getValorEnPuntos() <= puntosColab) {
                colaborador.getProductosOServiciosObtenidos().add(producto); // Agrega el producto
                colaborador.setPuntosAcumulados((double) - producto.getValorEnPuntos()); // Descuenta los puntos

                try {
                    colaboradorRepository.actualizar(colaborador);
                    System.out.println("Compra realizada con éxito.");
                } catch (Exception e) {
                    System.err.println("Error al actualizar el colaborador: " + e.getMessage());
                    return false;
                }
            } else {
                System.out.println("No te alcanzan los puntos");
                return false;
            }
        } else {
            System.out.println("Producto no encontrado");
            return false;
        }
        return true;
    }

    public void iniciarBroker() {

    }
}
