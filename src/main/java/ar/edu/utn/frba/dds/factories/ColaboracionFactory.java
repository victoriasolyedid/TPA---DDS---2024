package ar.edu.utn.frba.dds.factories;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.ColaboracionController;
import ar.edu.utn.frba.dds.controllers.ColaboradorController;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.donacionDeDinero.DonacionDeDinero;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.hacerseCargoDeHeladera.HacerseCargoDeHeladera;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable.PersonaSitVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable.RegistrarPersonaSitVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DistribucionDeVianda;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.MotivoDistribucion;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladeras.sensor.ModeloHeladera;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.*;
import ar.edu.utn.frba.dds.models.entities.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.ModeloHeladeraRepository;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ColaboracionFactory {
    public static TipoDeColaboracion crear(Context ctx, HeladerasRepository heladerasRepository) {
        String tipoColabo = ctx.formParam("tipo_colab");
        TipoDeColaboracion colabNueva = null;

        Usuario usuario = ctx.sessionAttribute("user");
        Colaborador colaborador = usuario.getColaborador();

        System.out.println("Tipo de colaboración: " + tipoColabo);
        System.out.println("Colaborador: " + colaborador.getNombre());

        switch (Objects.requireNonNull(tipoColabo)) {
            case "donacion-dinero":
                boolean periodica = ctx.formParam("frecuencia") != null;
                colabNueva = DonacionDeDinero.builder()
                        .monto(Integer.valueOf(Objects.requireNonNull(ctx.formParam("monto"))))
                        .periodica(periodica)
                        // Si periodica es false no debería poder completarse "frecuencia"
                        .frecuencia(ctx.formParam("frecuencia"))
                        .build();
                break;
            case "donacion-vianda":
                Optional<Heladera> heladeraDonacion = heladerasRepository.buscarPorNombre(ctx.formParam("heladera"));
                if (heladeraDonacion.isEmpty()) {
                    throw new IllegalArgumentException("Heladera no encontrada");
                }

                Vianda vianda = Vianda.builder()
                        .comida((ctx.formParam("nombre_comida")))
                        .fechaCaducidad(LocalDate.parse(Objects.requireNonNull(ctx.formParam("vto_comida"))).atStartOfDay())
                        // .fechaDonacion(LocalDate.parse(Objects.requireNonNull(ctx.formParam("fechaDonacionDeViandas"))).atStartOfDay())
                        .heladera(heladeraDonacion.get())
                        // .calorias() TODO: ver si ponerlo
                        // .peso() TODO: ver si ponerlo
                        .entregado(obtenerValorBooleano(ctx.formParam("entregada")))
                        .build();

                colabNueva = DonacionDeViandas.builder().viandas(new ArrayList<>()).build();
                ((DonacionDeViandas) colabNueva).agregarViandas(vianda);
                ((DonacionDeViandas) colabNueva).setHeladera(heladeraDonacion.get());
                // TODO: cantidad de viandas lo dejamos??
                break;
            case "distribucion-viandas":
                Optional<Heladera> heladeraOrigen = heladerasRepository.buscarPorNombre(ctx.formParam("heladera_origen"));
                Optional<Heladera> heladeraDestino = heladerasRepository.buscarPorNombre(ctx.formParam("heladera_destino"));
                if (heladeraOrigen.isEmpty() || heladeraDestino.isEmpty()) {
                    throw new IllegalArgumentException("Heladera no encontrada");
                }

                colabNueva = DistribucionDeVianda.builder()
                        .cantViandas(Integer.valueOf(Objects.requireNonNull(ctx.formParam("cantidad"))))
                        .heladeraOrigen(heladeraOrigen.get())
                        .heladeraDestino(heladeraDestino.get())
                        .motivo(MotivoDistribucion.valueOf(Objects.requireNonNull(ctx.formParam("motivo"))))
                        .build();
                break;
            case "alta-persona":
                Ubicacion ubicacion = null;
                if(obtenerValorBooleano(ctx.formParam("posee_dom"))) {
                    String provinciaCode = ctx.formParam("provincia_vulnerable");
                    Provincia provincia = new Provincia(ProvinciaMapper.getNombreProvincia(provinciaCode));

                    String localidadCode = ctx.formParam("localidad_vulnerable");
                    Localidad localidad = new Localidad(LocalidadMapper.getNombreLocalidad(localidadCode));

                    ubicacion = Ubicacion.builder()
                            .provincia(provincia)
                            .localidad(localidad)
                            .calle(ctx.formParam("calle_vulnerable"))
                            .altura((Objects.requireNonNull(ctx.formParam("altura_vulnerable"))))
                            .build();
                }
                if (ubicacion != null) {
                    ubicacion.buscarCoordenadas();
                }

                Tarjeta nuevaTarjeta = new Tarjeta(colaborador);

                PersonaSitVulnerable persona = PersonaSitVulnerable.builder()
                        .nombre(ctx.formParam("nombre"))
                        .apellido(ctx.formParam("apellido"))
                        .tipoDocumento(TipoDocumento.valueOf(Objects.requireNonNull(ctx.formParam("tipo_doc"))))
                        .documento(ctx.formParam("num_doc"))
                        .fechaNacimiento(LocalDate.parse(Objects.requireNonNull(ctx.formParam("fecha_nacimiento"))).atStartOfDay())
                        .fechaRegistro(LocalDate.parse(Objects.requireNonNull(ctx.formParam("fecha_registro"))).atStartOfDay())
                        .situacionCalle(estaEnSitDeCalle(ctx.formParam("posee_dom")))
                        // Si está en situación de calle no se debería poder completar el domicilio
                        .domicilio(ubicacion)
                        .tieneMenoresACargo(obtenerValorBooleano(ctx.formParam("posee_menores")))
                        .menoresACargo(Integer.valueOf((obtenerValorBooleano(ctx.formParam("posee_menores")) ? Objects.requireNonNull(ctx.formParam("cant_menores")) : "0")))
                        .tarjeta(nuevaTarjeta)
                        .build();

                Integer cantViandas = persona.inicializarCantViandasRestantes();
                persona.setCantViandasRestantes(cantViandas);

                colabNueva = new RegistrarPersonaSitVulnerable(persona);
                break;
            case "hacerse-cargo-heladera":
                String provinciaCode = ctx.formParam("provincia_heladera");
                Provincia prov = new Provincia(ProvinciaMapper.getNombreProvincia(provinciaCode));

                String localidadCode = ctx.formParam("localidad_heladera");
                Localidad loc = new Localidad(LocalidadMapper.getNombreLocalidad(localidadCode));

                Ubicacion ubi = Ubicacion.builder()
                        .provincia(prov)
                        .localidad(loc)
                        .calle(ctx.formParam("calle_heladera"))
                        .altura((Objects.requireNonNull(ctx.formParam("altura_heladera"))))
                        .build();

                ModeloHeladera modelo = ServiceLocator.instanceOf(ModeloHeladeraRepository.class).buscarPorNombre(ctx.formParam("modelo_heladera")).get();
                Heladera nuevaHeladera = Heladera.builder()
                        .ubicacion(ubi)
                        .capacidad(Integer.valueOf(Objects.requireNonNull(ctx.formParam("capacidad_heladera"))))
                        .fechaFuncionamiento(LocalDate.parse(Objects.requireNonNull(ctx.formParam("fecha_funcionamiento_heladera"))))
                        .activa(true)
                        .nombre(ctx.formParam("nombre_heladera"))
                        .cantViandas(0)
                        .modelo(modelo)
                        .build();

                colabNueva = new HacerseCargoDeHeladera(nuevaHeladera);
                break;
            default:
                throw new IllegalArgumentException("Tipo de colaboración no encontrado");
        }
        // TODO: terminar de probar

        colaborador.agregarColaboracion(colabNueva);
        Double nuevoPuntaje = colabNueva.puntajeAsociado();
        colaborador.setPuntosAcumulados(nuevoPuntaje);

        ColaboradorController colaboradorController = ServiceLocator.instanceOf(ColaboradorController.class);
        colaboradorController.actualizar(colaborador);

        colabNueva.setColaborador(colaborador);
        colabNueva.setFechaColaboracion(LocalDateTime.now());

        return colabNueva;
    }
    public static Boolean obtenerValorBooleano(String valor) {
        return Objects.equals(valor, "true");
    }

    public static Boolean estaEnSitDeCalle(String valor) {
        return !Objects.equals(valor, "true");
    }
}
