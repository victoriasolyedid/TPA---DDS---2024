package ar.edu.utn.frba.dds.models.entities.importador;

import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.validador.CustomException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorCamposCSV {

    public Boolean realizarValidaciones(String[] partes, int nroDelinea) {
        try {
            if (verificarTipoDoc(partes[0]) &&
                    verificarNumeroDoc(partes[1]) &&
                    verificarNombre(partes[2]) &&
                    verificarApellido(partes[3]) &&
                    verificarMail(partes[4]) &&
                    verificarFecha(partes[5]) &&
                    verificarFormaColab(partes[6]))
            {
                return  true;
            }
        } catch (CustomException e) {
            System.out.println("Error en la linea " + nroDelinea + ": " + e.getMessage());
        }
        return false;
    }

    public Boolean verificarTipoDoc(String tipoDoc) throws CustomException {
        if (    !tipoDoc.equals(TipoDocumento.DNI.toString()) &&
                !tipoDoc.equals(TipoDocumento.LC.toString()) &&
                !tipoDoc.equals(TipoDocumento.LE.toString())) {
            throw new CustomException("El tipo de documento no es válido.");
        } else return true;
    }

    public Boolean verificarNumeroDoc(String numeroDoc) throws CustomException {
        if (!numeroDoc.matches("\\d+") || !(numeroDoc.length() <= 10)) {
            throw new CustomException("El numero de documento no es válido.");
        } else return true;
    }

    public Boolean verificarNombre(String nombre) throws CustomException {
        if (!nombre.matches("[a-zA-Z]+") || !(nombre.length() <= 50)) {
            throw new CustomException("El nombre no es válido.");
        } else return true;
    }

    public Boolean verificarApellido(String apellido) throws CustomException {
        if (!apellido.matches("[a-zA-Z]+") || !(apellido.length() <= 50)) {
            throw new CustomException("El apellido no es válido.");
        } else return true;
    }

    public Boolean validarFormatoMail(String email) {
        // Definimos la expresión regular para un email
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        // Compilamos la expresión regular en un patrón
        Pattern pattern = Pattern.compile(regex);
        // Creamos un matcher que comparará el patrón contra el email
        Matcher matcher = pattern.matcher(email);

        // Verificamos si el email coincide con el patrón
        return matcher.matches();
    }

    public Boolean verificarMail(String email) throws CustomException {
        if (!validarFormatoMail(email) || !(email.length() <= 50)) {
            throw new CustomException("El mail no es válido.");
        } else return true;
    }

    private Boolean fechaEsValida(String fecha, DateTimeFormatter formatter) {
        try {
            LocalDate parsedDate = LocalDate.parse(fecha, formatter);
            // ParsedDate es una fecha válida (p.ej., 30 de febrero no será válida)
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public Boolean verificarFecha(String fecha) throws CustomException {
        // Formateador de fecha para analizar la fecha
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (!fechaEsValida(fecha, dateFormatter)) {
            throw new CustomException("La fecha no es válida.");
        } else return true;
    }

    public Boolean verificarFormaColab(String tipoColaboracion) throws CustomException {
        if (    !tipoColaboracion.equals("DINERO") &&
                !tipoColaboracion.equals("DONACION_VIANDAS") &&
                !tipoColaboracion.equals("REDISTRIBUCION_VIANDAS") &&
                !tipoColaboracion.equals("ENTREGA_TARJETAS"))
        {
            throw new CustomException("El tipo de colaboración no es válido.");
        } else return true;
    }
}