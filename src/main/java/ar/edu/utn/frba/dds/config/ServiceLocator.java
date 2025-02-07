package ar.edu.utn.frba.dds.config;

import ar.edu.utn.frba.dds.controllers.*;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.Mail;
import ar.edu.utn.frba.dds.models.entities.incidentes.HeladeraBroker2;
import ar.edu.utn.frba.dds.models.repositories.*;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<String, Object> instances = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T instanceOf(Class<T> componentClass) {
        String componentName = componentClass.getName();

        if (!instances.containsKey(componentName)) {
            Object instance = createInstance(componentClass);
            if (instance != null) {
                instances.put(componentName, instance);
            }
        }
        return (T) instances.get(componentName);
    }

    private static Object createInstance(Class<?> componentClass) {
        switch (componentClass.getName()) {
            case "ar.edu.utn.frba.dds.controllers.AperturaController":
                return new AperturaController(instanceOf(AperturasRepository.class));
            case "ar.edu.utn.frba.dds.controllers.AuthController":
                return new AuthController(new HeladeraBroker2());
            case "ar.edu.utn.frba.dds.controllers.ReportesController":
                return new ReportesController();
            case "ar.edu.utn.frba.dds.controllers.ColaboracionController":
                return new ColaboracionController(instanceOf(ColaboracionRepository.class), instanceOf(HeladerasRepository.class), instanceOf(ModeloHeladeraRepository.class), instanceOf(ColaboradorRepository.class), new HeladeraBroker2());
            case "ar.edu.utn.frba.dds.controllers.ColaboradorController":
                return new ColaboradorController(instanceOf(ColaboradorRepository.class));
            case "ar.edu.utn.frba.dds.controllers.FormularioController":
                return new FormularioController(instanceOf(FormulariosRepository.class));
            case "ar.edu.utn.frba.dds.controllers.FormularioCompletoController":
                return new FormularioCompletoController(instanceOf(FormularioCompletoRepository.class));
            case "ar.edu.utn.frba.dds.controllers.HeladeraController":
                return new HeladeraController(instanceOf(HeladerasRepository.class), instanceOf(SuscripcionRepository.class), instanceOf(SolicitudController.class), instanceOf(ColaboracionController.class), instanceOf(SuscripcionController.class), instanceOf(TecnicoController.class), new Mail());
            case "ar.edu.utn.frba.dds.controllers.IncidenteController":
                return new IncidenteController(instanceOf(IncidentesRepository.class), instanceOf(HeladerasRepository.class), instanceOf(ColaboracionRepository.class), instanceOf(TecnicoController.class), instanceOf(TecnicosRepository.class), instanceOf(TrabajoPendienteRepository.class));
            case "ar.edu.utn.frba.dds.controllers.ProductoController":
                return new ProductoController(instanceOf(ColaboracionRepository.class), instanceOf(ColaboradorController.class));
            case "ar.edu.utn.frba.dds.controllers.SolicitudController":
                return new SolicitudController(instanceOf(SolicitudesRepository.class), instanceOf(HeladerasRepository.class), instanceOf(ColaboracionRepository.class), instanceOf(AperturasRepository.class));
            case "ar.edu.utn.frba.dds.controllers.SuscripcionController":
                return new SuscripcionController(instanceOf(MessagesRepository.class), instanceOf(HeladerasRepository.class), instanceOf(SuscripcionRepository.class));
            case "ar.edu.utn.frba.dds.controllers.TecnicoController":
                return new TecnicoController(instanceOf(TecnicosRepository.class));
            case "ar.edu.utn.frba.dds.controllers.UsuarioController":
                return new UsuarioController(instanceOf(UsuariosRepository.class));
            case "ar.edu.utn.frba.dds.controllers.VisitaTecnicoController":
                return new VisitaTecnicoController(instanceOf(VisitasTecnicoRepository.class));
            case "ar.edu.utn.frba.dds.models.repositories.AperturasRepository":
                return new AperturasRepository();
            case "ar.edu.utn.frba.dds.models.repositories.ColaboracionRepository":
                return new ColaboracionRepository();
            case "ar.edu.utn.frba.dds.models.repositories.ColaboradorRepository":
                return new ColaboradorRepository();
            case "ar.edu.utn.frba.dds.models.repositories.FormulariosRepository":
                return new FormulariosRepository();
            case "ar.edu.utn.frba.dds.models.repositories.FormularioCompletoRepository":
                return new FormularioCompletoRepository();
            case "ar.edu.utn.frba.dds.models.repositories.HeladerasRepository":
                return new HeladerasRepository();
            case "ar.edu.utn.frba.dds.models.repositories.IncidentesRepository":
                return new IncidentesRepository();
            case "ar.edu.utn.frba.dds.models.repositories.MessagesRepository":
                return new MessagesRepository();
            case "ar.edu.utn.frba.dds.models.repositories.ModeloHeladeraRepository":
                return new ModeloHeladeraRepository();
            case "ar.edu.utn.frba.dds.models.repositories.ProductoServicioRepository":
                return new ProductoServicioRepository();
            case "ar.edu.utn.frba.dds.models.repositories.SensorMovimientoRepository":
                return new SensorMovimientoRepository();
            case "ar.edu.utn.frba.dds.models.repositories.SensorTemperaturaRepository":
                return new SensorTemperaturaRepository();
            case "ar.edu.utn.frba.dds.models.repositories.SolicitudesRepository":
                return new SolicitudesRepository();
            case "ar.edu.utn.frba.dds.models.repositories.TecnicosRepository":
                return new TecnicosRepository();
            case "ar.edu.utn.frba.dds.models.repositories.TrabajoPendienteRepository":
                return new TrabajoPendienteRepository();
            case "ar.edu.utn.frba.dds.models.repositories.UsuariosRepository":
                return new UsuariosRepository();
            case "ar.edu.utn.frba.dds.models.repositories.VisitasTecnicoRepository":
                return new VisitasTecnicoRepository();
            case "ar.edu.utn.frba.dds.models.repositories.SuscripcionRepository":
                return new SuscripcionRepository();
            default:
                return null;
        }
    }
}