package ar.edu.utn.frba.dds.models.repositories.mains;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.UsuarioController;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.formularios.Formulario;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.entities.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Localidad;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Provincia;
import ar.edu.utn.frba.dds.models.entities.info.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.repositories.UsuariosRepository;
import ar.edu.utn.frba.dds.roles.TipoRol;

import java.util.Arrays;

public class MiniMainDePrueba {
    public static void main(String[] args) {
        UsuariosRepository usuariosRepository = ServiceLocator.instanceOf(UsuariosRepository.class);

        // Instanciamos forms

        // Formulario formHumana = new Formulario("formHumana");
        // Formulario formJuridica = new Formulario("formJuridica");
        Formulario formAdmin = new Formulario("formAdmin");

        // formHumana.setCampos(Arrays.asList("nombre", "apellido", "tipo_documento", "numero_documento", "whatsapp", "telefono", "fecha_nacimiento", "direccion"));
        // formJuridica.setCampos(Arrays.asList("razon_social", "rubro", "tipo_empresa", "whatsapp_jur", "telefono_jur", "direccion_jur"));
        formAdmin.setCampos(Arrays.asList("nombre", "fecha_de_alta"));

        // Completamos un formulario para el admin
        FormularioCompleto formCompletoAdmin = new FormularioCompleto();
        formCompletoAdmin.getCamposCompletos().put("nombre", "Admin");
        formCompletoAdmin.getCamposCompletos().put("fecha_de_alta", "2024-06-15");

        // Creamos un Colaborador para el admin porque sino se rompe
        Colaborador colabAdmin = new Colaborador("admin@gmail.com", false, formCompletoAdmin);

        // Creamos un usuario de tipo admin para guardar en la DB
        Usuario usuarioAdmin = new Usuario("admin", "admin-761", TipoRol.ADMIN, colabAdmin, null);

        // Ubicaciones para las heladeras
        /*Provincia provincia1 = new Provincia();
        Provincia provincia2 = new Provincia();

        Localidad localidad1 = new Localidad();
        Localidad localidad2 = new Localidad();

        // (provincia, localidad, calle, altura)
        Ubicacion ubicacion1 = new Ubicacion();
        Ubicacion ubicacion2 = new Ubicacion();

        // Algunas heladeras para tener guardadas
        // (ubicacion, capacidad, fecha funcionamiento, nombre)
        Heladera heladera1 = new Heladera();
        Heladera heladera2 = new Heladera();*/

        usuariosRepository.guardar(usuarioAdmin);
    }
}
