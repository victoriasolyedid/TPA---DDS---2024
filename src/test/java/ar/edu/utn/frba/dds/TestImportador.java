//package ar.edu.utn.frba.dds;
//
//import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
//import ar.edu.utn.frba.dds.models.entities.importador.EmailSender;
//import ar.edu.utn.frba.dds.models.entities.importador.Importador;
//import ar.edu.utn.frba.dds.models.entities.importador.ValidadorCamposCSV;
//import ar.edu.utn.frba.dds.models.entities.info.TipoDocumento;
//import ar.edu.utn.frba.dds.models.entities.validador.CustomException;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.junit.jupiter.api.Assertions.*;
////import static org.junit.jupiter.api.Assertions.assertEquals;
////import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class TestImportador {
//
//    // **** TIPO DE DOCUMENTO ****
//
//    @Test
//    // Testeo de un tipo de documento NO VALIDO
//    public void documentoInvalido() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarTipoDoc("PASAPORTE");
//        });
//
//        assertEquals("El tipo de documento no es válido.", exception.getMessage());
//    }
//
//    @Test
//    // Testeo de un tipo de documento VALIDO: DNI
//    public void documentoValidoDNI() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarTipoDoc("DNI"));
//        });
//    }
//
//    @Test
//    // Testeo de un tipo de documento VALIDO: LC
//    public void documentoValidoLC() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarTipoDoc("LC"));
//        });
//    }
//
//    @Test
//    // Testeo de un tipo de documento VALIDO: LE
//    public void documentoValidoLE() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarTipoDoc("LE"));
//        });
//    }
//
//    // **** NUMERO DE DOCUMENTO ****
//
//    @Test
//    // Testeo de un numero de documento invalido por tener mas de 10 digitos
//    public void documentoConMuchosDigitos() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarNumeroDoc("649274918263");
//        });
//
//        assertEquals("El numero de documento no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un numero de documento invalido por tener caracteres incorrectos
//    @Test
//    public void documentoConCaracteresInvalidos() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarNumeroDoc("649A5b78");
//        });
//
//        assertEquals("El numero de documento no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un numero de documento VALIDO
//    @Test
//    public void documentoValido() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarNumeroDoc("20204186"));
//        });
//    }
//
//    // **** NOMBRE ****
//
//    // Testeo de un nombre invalido por tener numeros
//    @Test
//    public void nombreConNumeros() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarNombre("Margarita24");
//        });
//
//        assertEquals("El nombre no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un nombre invalido por tener mas de 50 digitos
//    @Test
//    public void nombreConMuchosCaracteres() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarNombre("qwertyuiopkjhgfdsazxcvbnmqwertyuiopasdfghjklñzxcvbnm");
//        });
//
//        assertEquals("El nombre no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un nombre VALIDO
//    @Test
//    public void nombreValido() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarNombre("Margarita"));
//        });
//    }
//
//    // **** APELLIDO ****
//    // Testeo de un apellido invalido por tener numeros
//    @Test
//    public void apellidoConNumeros() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarApellido("Margarita24");
//        });
//
//        assertEquals("El apellido no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un apellido invalido por tener mas de 50 digitos
//    @Test
//    public void apellidoConMuchosCaracteres() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarApellido("qwertyuiopkjhgfdsazxcvbnmqwertyuiopasdfghjklñzxcvbnm");
//        });
//
//        assertEquals("El apellido no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un apellido VALIDO
//    @Test
//    public void apellidoValido() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarApellido("Lopez"));
//        });
//    }
//
//    // **** MAIL ****
//
//    // Testeo de un mail invalido por tener mas de 50 digitos
//    @Test
//    public void mailInvalido() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarMail("qwertyuiopkjhgfdsazxcv@bnmqwertyuiopasdfghjklñzxcvbnm");
//        });
//
//        assertEquals("El mail no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un mail invalido por no cumplir con el formato
//    @Test
//    public void mailSinDominio() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarMail("mailinvalido");
//        });
//
//        assertEquals("El mail no es válido.", exception.getMessage());
//    }
//
//    // Testeo de un mail VALIDO
//    @Test
//    public void mailValido() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarMail("alumno@gmail.com"));
//        });
//    }
//
//    // **** FECHA ****
//
//    // Testeo de fecha invalida por no cumplir con el formato
//    @Test
//    public void fechaInvalida() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarFecha("2019/01/23");
//        });
//
//        assertEquals("La fecha no es válida.", exception.getMessage());
//    }
//
//    // Testeo de fecha invalida por no cumplir con el formato
//    @Test
//    public void fechaSinFormato() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarFecha("20190123");
//        });
//
//        assertEquals("La fecha no es válida.", exception.getMessage());
//    }
//
//    // Testeo de una fecha VALIDA
//    @Test
//    public void fechaValida() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarFecha("25/05/2010"));
//        });
//    }
//
//    // **** FORMA DE COLABORACION ****
//
//    // Testeo de forma de colaboracion invalida
//    @Test
//    public void formaColaboracionInvalida() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        CustomException exception = assertThrows(CustomException.class, () -> {
//            validador.verificarFormaColab("COMPRAR");
//        });
//
//        assertEquals("El tipo de colaboración no es válido.", exception.getMessage());
//    }
//
//    // Testeo de una forma de colaboracion VALIDA: DINERO
//    @Test
//    public void formaColaboracionDinero() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarFormaColab("DINERO"));
//        });
//    }
//
//    // Testeo de una forma de colaboracion VALIDA: DONACION_VIANDAS
//    @Test
//    public void formaColaboracionDonacionDeViandas() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarFormaColab("DONACION_VIANDAS"));
//        });
//    }
//
//    // Testeo de una forma de colaboracion VALIDA: REDISTRIBUCION_VIANDAS
//    @Test
//    public void formaColaboracionRedistrViandas() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarFormaColab("REDISTRIBUCION_VIANDAS"));
//        });
//    }
//
//    // Testeo de una forma de colaboracion VALIDA: ENTREGA_TARJETAS
//    @Test
//    public void formaColaboracionValidaEntregaTarjetas() {
//        ValidadorCamposCSV validador = new ValidadorCamposCSV();
//
//        assertDoesNotThrow(() -> {
//            assertTrue(validador.verificarFormaColab("ENTREGA_TARJETAS"));
//        });
//    }
//
//    // **** IMPORTAR COLABORACIONES ****
//
//    @Test
//    public void colaboracionesImportadas() {
//        Importador importador = new Importador();
//        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
//        importador.setEmailSender(emailSenderMock);
//
//        importador.leerArchivo("src/main/java/ar/edu/utn/frba/dds/importador/pruebaImportador.csv");
//
//        assertEquals(4,importador.getColaboracionesImportadas().size());
//    }
//
//    @Test
//    public void colaboracionesImportadasConErrores() {
//        Importador importador = new Importador();
//        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
//        importador.setEmailSender(emailSenderMock);
//
//        importador.leerArchivo("src/main/java/ar/edu/utn/frba/dds/importador/pruebaImportador2.csv");
//
//        assertEquals(1,importador.getColaboracionesImportadas().size());
//    }
//
//    // Test para verificar que se guarda la cantidad de colaboraciones que corresponde al colaborador
//    @Test
//    public void cantidadDeColabDeUnColaborador() {
//        Importador importador = new Importador();
//        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
//        importador.setEmailSender(emailSenderMock);
//
//        importador.leerArchivo("src/main/java/ar/edu/utn/frba/dds/importador/pruebaImportador3.csv");
//
//        Colaborador colaborador = importador.buscarColaborador(TipoDocumento.DNI, 10574727);
//
//        assertEquals(5,colaborador.getColaboraciones().size());
//    }
//}
