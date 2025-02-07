package ar.edu.utn.frba.dds.models.entities.importador;

import ar.edu.utn.frba.dds.config.ServiceLocator;
import ar.edu.utn.frba.dds.controllers.ColaboracionController;
import ar.edu.utn.frba.dds.controllers.ColaboradorController;
import ar.edu.utn.frba.dds.controllers.FormularioCompletoController;
import ar.edu.utn.frba.dds.controllers.UsuarioController;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.TipoDeColaboracion;
import ar.edu.utn.frba.dds.models.entities.envioDeMensajes.TipoMedioContacto;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.donacionDeDinero.DonacionDeDinero;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.personaSitVulnerable.RegistrarPersonaSitVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DistribucionDeVianda;
import ar.edu.utn.frba.dds.models.entities.colaboraciones.viandas.DonacionDeViandas;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.formularios.FormularioCompleto;
import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.roles.TipoRol;
import lombok.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Importador {
    private BufferedReader lector; // lee el archivo
    private String linea; // recibe la linea de cada fila
    private String partes[] = null; // almacena cada linea leida

    private ValidadorCamposCSV validador = new ValidadorCamposCSV();
    private PasswordGenerator passwordGenerator = new PasswordGenerator();
    @Setter
    private EmailSender emailSender;

    public void leerArchivo(InputStream inputStream) {
        int nroDeLinea = 0;
        try {
            lector = new BufferedReader(new InputStreamReader(inputStream));
            while ((linea = lector.readLine()) != null) {
                nroDeLinea++;
                if(nroDeLinea == 1) {
                    continue;
                }
                partes = linea.split(",");
                if (validador.realizarValidaciones(partes,nroDeLinea)) {
                    for(int i=1 ; i<=Integer.parseInt(partes[7]) ; i++) {
                        instanciarColaborador(partes);
                    }
                }
            }
            lector.close();
            linea = null;
            partes = null;
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
        }
    }

    public Colaborador buscarColaborador(String mail) {
        Optional<Colaborador> colaborador = ServiceLocator.instanceOf(ColaboradorController.class).buscarPorMail(mail);
        // Optional<Colaborador> colaborador = ServiceLocator.instanceOf(ColaboradorController.class).buscarPorDocumento(tipoDoc, nroDoc);
        return colaborador.orElse(null);
    }

    // TODO: implementar con lo que ya tenemos
    public void enviarCorreoColaboradorNuevo(Colaborador colaborador, EmailSender emailSender) {
        // Lógica para enviar correo electrónico al colaborador nuevo
        String mensaje = "Estimado " + colaborador.getNombre() + ", Gracias por unirte como colaborador. " +
                        "Adjuntamos sus credenciales de acceso al sistema. " +
                        //"Usuario: " + colaborador.getUsuario().getNombreUsuario() +
                        //"Contrasenia: " + colaborador.getUsuario().getContrasenia() +
                        "Por favor, inicie sesión para completar sus datos.";
        emailSender.enviarCorreo( colaborador.getMedioDeContacto().stream()
                .filter(medio -> medio.getTipo().equals(TipoMedioContacto.MAIL))
                .findFirst()
                .orElse(null).getValor(), mensaje);
    }

    public void generarUsuario(Colaborador colaborador, String mail) {
        // Ponemos como nombre de usuario lo que está antes del @
        String nuevoUsername = mail.substring(0, mail.indexOf('@'));

        // Creamos el usuario
        Usuario usuario = new Usuario(nuevoUsername, passwordGenerator.generatePassword(12), TipoRol.HUMANA, colaborador, null);

        // Guardamos el usuario
        UsuarioController usuarioController = ServiceLocator.instanceOf(UsuarioController.class);
        usuarioController.guardarCargaMasiva(usuario);
    }

    public LocalDateTime formattearFecha(String fecha) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDateTime.of(LocalDate.parse(fecha, formatter), LocalTime.MIDNIGHT);
    }

    public void instanciarColaborador(String[] partes) {
        String mail = String.valueOf(partes[4]);

        // (X) Se debería buscar por documento PERO no se pueden armar los query por como tenemos guardados los datos
        Colaborador colaborador = buscarColaborador(mail);

        // Si el Colaborador es nuevo en nuestro sistema, hay que instanciarlo y crearle un usuario
        if (colaborador == null) {
            // Instanciamos un formulario completo para guardar los datos
            FormularioCompleto formCompleto = new FormularioCompleto();
            formCompleto.completarCampo("tipo_documento",partes[0]);
            formCompleto.completarCampo("numero_documento",partes[1]);
            formCompleto.completarCampo("nombre",partes[2]);
            formCompleto.completarCampo("apellido",partes[3]);
            formCompleto.completarCampo("mail",partes[4]);

            // Guardamos el formulario
            FormularioCompletoController formularioCompletoController = ServiceLocator.instanceOf(FormularioCompletoController.class);
            formularioCompletoController.guardarCargaMasiva(formCompleto);

            // Instanciamos al Colaborador y genaramos el usuario (siempre son personas humanas)
            colaborador = new Colaborador(mail, false, formCompleto);
            this.generarUsuario(colaborador, mail);

            // enviarCorreoColaboradorNuevo(colaborador, emailSender);
        } instanciarColaboraciones(partes, colaborador);
    }

    public void instanciarColaboraciones(String[] partes, Colaborador colaborador) {
        TipoDeColaboracion colaboracion = null;

        switch (partes[6]) {
            case "DINERO":
                colaboracion = new DonacionDeDinero();
                break;
            case "DONACION_VIANDAS":
                colaboracion = new DonacionDeViandas();
                break;
            case "REDISTRIBUCION_VIANDAS":
                colaboracion = new DistribucionDeVianda();
                break;
            case "ENTREGA_TARJETAS":
                colaboracion = new RegistrarPersonaSitVulnerable();
                break;
        }
        colaborador.agregarColaboracion(colaboracion);

        // (X) El puntaje asociado no lo puedo cargar porque no tenemos datos de las colaboraciones
        // Double nuevoPuntaje = colaboracion.puntajeAsociado();
        // colaborador.setPuntosAcumulados(nuevoPuntaje);

        ColaboradorController colaboradorController = ServiceLocator.instanceOf(ColaboradorController.class);
        colaboradorController.actualizar(colaborador);

        colaboracion.setColaborador(colaborador);
        colaboracion.setFechaColaboracion(formattearFecha(partes[5]));

        ColaboracionController colaboracionController = ServiceLocator.instanceOf(ColaboracionController.class);
        colaboracionController.guardarCargaMasiva(colaboracion);
    }
}