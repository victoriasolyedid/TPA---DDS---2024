package ar.edu.utn.frba.dds.models.entities.importador;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {
    // Definir el conjunto de caracteres permitidos
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-=+[]{}|;:'\",.<>?/";

    // Conjunto de caracteres completo
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARS;

    // Instancia de SecureRandom para generar números aleatorios
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // Método para generar una contraseña aleatoria
    public String generatePassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("El largo de la contraseña debe ser mayor que 0.");
        }

        StringBuilder password = new StringBuilder(length);

        // Asegurar que la contraseña tenga al menos un carácter de cada tipo
        password.append(LOWERCASE.charAt(SECURE_RANDOM.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(SECURE_RANDOM.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(SECURE_RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(SECURE_RANDOM.nextInt(SPECIAL_CHARS.length())));

        // Generar el resto de la contraseña
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(SECURE_RANDOM.nextInt(ALL_CHARS.length())));
        }

        // Mezclar los caracteres de la contraseña para mayor aleatoriedad
        return shuffleString(password.toString());
    }
    // Método para mezclar los caracteres de una cadena
    private static String shuffleString(String input) {
        List<Character> characters = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(characters, SECURE_RANDOM);
        StringBuilder result = new StringBuilder(characters.size());
        characters.forEach(result::append);
        return result.toString();
    }
}