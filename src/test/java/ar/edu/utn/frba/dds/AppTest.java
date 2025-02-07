package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.models.entities.usuario.Usuario;
import ar.edu.utn.frba.dds.models.entities.validador.Validador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppTest {
    @Test
    // SIN MAYÚSCULAS: NO PASA
    public void contraseniaSinMayuscula() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "rosalia54!"));
    }

    @Test
    // SIN MINÚSCULAS: NO PASA
    public void contraseniaSinMinuscula() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "ROSALIA54!"));
    }

    @Test
    // SIN CARACTER_ESPECIAL: NO PASA
    public void contraseniaSinCaracterEspecial() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "ROSALIA54"));
    }

    @Test
    // SIN NÚMEROS: NO PASA
    public void contraseniaSinCaracterNumero() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "Rosalia!"));
    }

    @Test
    // NO CUMPLE CANTIDAD MINIMA DE CARACTERES: NO PASA
    public void contraseniaCorta() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "Ros!4"));
    }

    @Test
    // CUMPLE TODOS LOS REQUISITOS PERO ESTÁ EN LA LISTA DE LAS PEORES CONTRASEÑAS: NO PASA
    public void laContraseniaEstaEnLaLista() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "Rosalia54!"));
    }

    @Test
    // CUMPLE TODOS LOS REQUISITOS Y NO ESTÁ EN LA LISTA DE LAS PEORES CONTRASEÑAS: PASA
    public void elUsuarioCumpleConLosRequisitosYNoEstaEnLaLista() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertTrue(Validador.esContraseniaSegura(usuario, "Mrulip4!"));
    }

    @Test
    // COINCIDE CON USUARIO: NO PASA
    public void coincideConUsuario() {
        Usuario usuario = new Usuario("Mrulio4!", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "Mrulio4!"));
    }

    @Test
    // CONTIENE CARACTER REPETITIVO: NO PASA
    public void contieneCaracterRepetitivo() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "Aaa4roI!"));
    }

    @Test
    // COINCIDE CON USUARIO: NO PASA
    public void contieneCaracterSecuencial() {
        Usuario usuario = new Usuario("Usuario", "Mruli0.24!");
        Assertions.assertFalse(Validador.esContraseniaSegura(usuario, "Mrio234!"));
    }
}
