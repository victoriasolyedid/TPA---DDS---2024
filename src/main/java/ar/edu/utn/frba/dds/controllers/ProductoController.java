package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.factories.ProductoOServicioFactory;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.OfertaProductosOServicios;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.ofertaProductosOServicios.Rubro;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.ColaboracionRepository;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class ProductoController {
    private ColaboracionRepository colaboracionRepository;
    private ColaboradorController colaboradorController;

    public List<OfertaProductosOServicios> buscarTodos() {
        return this.colaboracionRepository.buscarProductosOServicios();
    }

    public void index(Context ctx) {
        List<OfertaProductosOServicios> productosOServicios = this.buscarTodos();

        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        if (usuario != null) {
            Double puntosAcumulados = usuario.getColaborador() != null ? usuario.getColaborador().getPuntosAcumulados() : 0.0;
            model.put("puntosAcumulados", puntosAcumulados);
            model.put("productos", productosOServicios);
        }

        ctx.render("catalogo/catalogo.hbs", model);
    }

    public void publicarView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();

        try {
            Rubro gastronomia = new Rubro("Gastronomia");
            Rubro electronica = new Rubro("Electronica");

            List<Rubro> rubros = Arrays.asList(gastronomia, electronica);
            model.put("rubros", rubros);
            ctx.render("catalogo/publicarProdServ.hbs", model);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Ocurrió un error al renderizar la vista.");
        }
    }

    public void crear(Context ctx) {

        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        if (usuario == null) {
            ctx.status(401).result("Usuario no autenticado.");
            return;
        }

        OfertaProductosOServicios productoOServicio = ProductoOServicioFactory.crear(ctx);
        colaboracionRepository.guardar(productoOServicio);
        ctx.redirect("/prod-serv");
    }

    public void canjearProducto(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario usuario = (Usuario) model.get("user");

        String productoId = ctx.formParam("productoId");


        Colaborador colaboradorActual = usuario.getColaborador();

        assert productoId != null;
        Optional<OfertaProductosOServicios> producto = this.colaboracionRepository.buscarProductoOServicio(Long.valueOf(productoId));

        // Verificamos si el producto existe y si el colaborador tiene suficientes puntos para realizar la compra
        if (producto.isPresent()) {
            // Realizamos la compra y actualizamos el colaborador
            if (colaboradorController.realizaCompra(colaboradorActual, producto)) {
                AuthController.actualizarUsuarioActual(ctx, usuario); // Refrescamos el usuario en la sesión
                ctx.redirect("/catalogo");
            } else {
                ctx.result("Error: El usuario no tiene suficiente puntos.");
            }
        } else {
            ctx.result("El producto solicitado no existe.");
        }
    }

    public void misProdServView(Context ctx) {
        Map<String, Object> model = ctx.sessionAttributeMap();
        Usuario user = (Usuario) model.get("user");
        Colaborador colaborador = user.getColaborador();
        List<OfertaProductosOServicios> ofertaProductosOServicios = colaborador.getProductosOServiciosObtenidos();
        if (user != null) {
            model.put("mis-prod-serv", ofertaProductosOServicios);
        }
        ctx.render("catalogo/gestionarProductos.hbs", model);
    }

}