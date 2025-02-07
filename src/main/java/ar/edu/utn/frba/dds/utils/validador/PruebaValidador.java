package ar.edu.utn.frba.dds.models.entities.validador;

import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.roles.TipoRol;

import java.io.IOException;
import java.util.Scanner;

public class PruebaValidador {
    public static void pruebaValidador(String[] args) throws IOException {

        Scanner teclado = new Scanner(System.in);
        String nombreUsuario;
        String contrasenia;

        System.out.println("Ingrese el nombre de usuario: ");
        nombreUsuario = teclado.nextLine();
        System.out.println("Ingrese la nueva contraseña: ");
        contrasenia = teclado.nextLine();
        Usuario usuario = new Usuario(nombreUsuario, contrasenia, TipoRol.HUMANA);
        //Validador.esContraseniaSegura(usuario);
    }

}
