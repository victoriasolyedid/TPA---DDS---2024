package ar.edu.utn.frba.dds.models.entities.validador;

import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {
    // Validar contraseña
    public static boolean esContraseniaSegura(Usuario usuario, String nuevaContrasenia) {
        if (!esContraseniaSeguraNuevoUsuario(nuevaContrasenia)) {
            return false;
        }
        try {
            if (coincideConUsuario(nuevaContrasenia, usuario.getNombreUsuario()) &&
                    esDistintaALaAnterior(nuevaContrasenia, usuario.getContrasenia())
            ) {
                System.out.println("La contraseña ha cumplido exitosamente con los requisitos.");
                return true;
            }
        } catch (CustomException e) {
            System.out.println("Contraseña inválida debido a: " +
                    e.getMessage());
        }
        return false;
    }

    public static boolean esContraseniaSeguraNuevoUsuario(String nuevaContrasenia) {
        try {
            if (cumpleCantidadDeCaracteres(nuevaContrasenia) &&
                    !seEncuentraEnElTopDeLasPeoresContrasenias(nuevaContrasenia) &&
                    !contieneCaracterRepetitivo(nuevaContrasenia) &&
                    !contieneCaracterSecuencial(nuevaContrasenia) &&
                    contieneDiversosCaracteres(nuevaContrasenia)
            ) {
                System.out.println("La contraseña ha cumplido exitosamente con los requisitos.");
                return true;
            }
        } catch (CustomException e) {
            System.out.println("Contraseña inválida debido a: " +
                    e.getMessage());
        }
        return false;
    }

    public static List<String> leerArchivo() {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "java", "ar", "edu", "utn", "frba", "dds", "models", "entities", "validador", "topPeoresContrasenias.txt");
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo");
        }
        return null;
    }

    public static boolean contieneDiversosCaracteres(String contrasenia) throws CustomException {
        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;
        boolean tieneCaracterEspecial = false;

        for (char c : contrasenia.toCharArray()) { //lo convierte en un vector de caracteres
            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            } else if (Character.isLowerCase(c)) {
                tieneMinuscula = true;
            } else if (Character.isDigit(c)) {
                tieneNumero = true;
            } else {
                tieneCaracterEspecial = true;
            }
        }

        if (!(tieneMayuscula && tieneMinuscula && tieneNumero && tieneCaracterEspecial)) {
            throw new CustomException("debe poseer como mínimo: " +
                    "Un caracter especial, un número," +
                    " una mayúscula, una minúsucla ");
        }

        return true;
    }

    public static boolean seEncuentraEnElTopDeLasPeoresContrasenias(String contrasenia) throws CustomException {
        int i = 0;
        for (String cadena : Objects.requireNonNull(leerArchivo())) {
            if (contrasenia.equals(cadena)) {
                i++;
            }
        }
        if (i != 0) {
            throw new CustomException("se encuentra en el top de las peores contraseñas.");
        } else return false;

    }

    public static boolean coincideConUsuario(String contrasenia, String nombreUsuario) throws CustomException {
        if (contrasenia.equals(nombreUsuario)) {
            throw new CustomException("no puede coincidir con el usuario");
        } else return true;
    }

    public static boolean contieneCaracterSecuencial(String contrasenia) throws CustomException {
        char[] caracteres = contrasenia.toCharArray();
        int j = 0;
        for (int i = 0; i < caracteres.length - 1; i++) {
            // Verificar si el siguiente carácter es secuencial al actual

            if (caracteres[i] + 1 == caracteres[i + 1]) {
                j++;
            }
        }
        if (j != 0) {
            throw new CustomException("no puede contener caracteres secuenciales.");

        } else return false;
    }

    public static boolean contieneCaracterRepetitivo(String contrasenia) throws CustomException {
        Pattern patron = Pattern.compile("(.)\\1+");
        Matcher matcher = patron.matcher(contrasenia);
        if (matcher.find()) {
            throw new CustomException("no puede contener caracteres repetitivos.");
        } else return false;
    }

    public static boolean cumpleCantidadDeCaracteres(String contrasenia) throws CustomException {
        if (contrasenia.length() < 8) {
            throw new CustomException("no es lo suficientemente larga.");
        } else return true;
    }

    public static boolean esDistintaALaAnterior(String contrasenia, String contraseniaAnterior) throws CustomException {
        if (contraseniaAnterior.equals(contrasenia)) {
            throw new CustomException("no puede coincidir con la anterior contraseña.");
        } else return true;
    }
}
