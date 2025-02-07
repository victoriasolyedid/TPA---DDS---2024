package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.models.entities.formularios.Formulario;

import java.util.ArrayList;
import java.util.List;

public class CamposToHTML {
    public static List<String> getHtml(Formulario formulario) {
        List<String> htmlCampos = new ArrayList<>();
        List<String> campos = formulario.getCampos();

        for (String campo : campos) {
            switch (campo) {
                case "nombre":
                    htmlCampos.add(generateInputHtml("text", "nombre", "Nombre", "col-md-6", false));
                    break;
                case "apellido":
                    htmlCampos.add(generateInputHtml("text", "apellido", "Apellido", "col-md-6", false));
                    break;
                case "tipo_documento":
                    htmlCampos.add(generateSelectHtml("tipo_documento", "Tipo de documento", new String[]{"DNI", "LC", "LE"}, "col-md-6"));
                    break;
                case "numero_documento":
                    htmlCampos.add(generateInputHtml("text", "numero_documento", "Número documento", "col-md-6", false));
                    break;
                case "whatsapp":
                    htmlCampos.add(generateInputHtml("text", "whatsapp", "WhatsApp", "col-6", false));
                    break;
                case "telefono":
                    htmlCampos.add(generateInputHtml("text", "telefono", "Teléfono", "col-md-6", false));
                    break;
                case "fecha_nacimiento":
                    htmlCampos.add(generateDateHtml("fecha_nacimiento", "Fecha de nacimiento", "col-md-12 d-flex align-content-center", false));
                    break;
                case "calle":
                    htmlCampos.add(generateInputHtml("text", "calle", "Calle", "col-md-8", false));
                    break;
                case "altura":
                    htmlCampos.add(generateInputHtml("text", "altura", "Altura", "col-md-4", false));
                    break;
                case "localidad":
                    htmlCampos.add(generateSelectLocalityHtml("localidad", "col-md-6"));
                    break;
                case "provincia":
                    htmlCampos.add(generateSelectProvinceHtml("provincia", "col-md-6"));
                    break;
                case "razon_social":
                    htmlCampos.add(generateInputHtml("text", "razon_social", "Razón Social", "col-md-12", false));
                    break;
                case "rubro":
                    htmlCampos.add(generateInputHtml("text", "rubro", "Rubro", "col-md-8", false));
                    break;
                case "tipo_empresa":
                    htmlCampos.add(generateSelectHtml("tipo_empresa", "Tipo", new String[]{"Gubernamental", "ONG", "Empresa", "Institución"}, "col-md-4"));
                    break;
                case "whatsapp_jur":
                    htmlCampos.add(generateInputHtml("text", "whatsapp_jur", "WhatsApp", "col-6", false));
                    break;
                case "telefono_jur":
                    htmlCampos.add(generateInputHtml("text", "telefono_jur", "Teléfono", "col-md-6", false));
                    break;
                case "calle_jur":
                    htmlCampos.add(generateInputHtml("text", "calle_jur", "Calle", "col-md-8", false));
                    break;
                case "altura_jur":
                    htmlCampos.add(generateInputHtml("text", "altura_jur", "Altura", "col-md-4", false));
                    break;
                case "localidad_jur":
                    htmlCampos.add(generateSelectLocalityHtml("localidad_jur", "col-md-6"));
                    break;
                case "provincia_jur":
                    htmlCampos.add(generateSelectProvinceHtml("provincia_jur", "col-md-6"));
                    break;
                default:
                    System.out.println("Campo no encontrado: " + campo + ". No se generará HTML para este campo.");
                    break;
            }
        }

        return htmlCampos;
    }

    private static String generateInputHtml(String type, String name, String placeholder, String cssClass, Boolean required) {
        return String.format("<div class='%s'>\n" +
                "    <input type='%s' class='form-control' name='%s' id='%s' placeholder='%s' %s />\n" +
                "</div>", cssClass, type, name, name, placeholder, required ? "required" : "");
    }

    private static String generateSelectHtml(String name, String placeholder, String[] options, String cssClass) {
        StringBuilder selectHtml = new StringBuilder(String.format("<div class='%s'>\n" +
                "    <select class='form-select' name='%s'>\n" +
                "        <option value='' selected>%s</option>\n", cssClass, name, placeholder));
        for (String option : options) {
            selectHtml.append(String.format("        <option value='%s'>%s</option>\n", option, option));
        }
        selectHtml.append("    </select>\n</div>");
        return selectHtml.toString();
    }

    private static String generateDateHtml(String name, String label, String cssClass, Boolean required) {
        return String.format("<div class='%s'>\n" +
                "    <span style='width: 300px; align-content: center;'>%s</span>\n" +
                "    <input type='date' class='form-control' name='%s' %s />\n" +
                "</div>", cssClass, label, name, required ? "required" : "");
    }

    private static String generateSelectLocalityHtml(String name, String cssClass) {
        return String.format("""
                <div class='%s'>
                    <select class='form-select' name='%s' id='%s' aria-label='Seleccionar Localidad'>
                        <option selected>Seleccionar Localidad</option>
                """, cssClass, name, name, name) + "    </select>\n" +
                "</div>";
    }

    private static String generateSelectProvinceHtml(String name, String cssClass) {
        return String.format("""
                <div class='%s'>
                    <select class='form-select' name='%s' id='%s' aria-label='Seleccionar Provincia' onchange='cargarLocalidades(event)'>
                        <option selected>Seleccionar Provincia</option>
                """, cssClass, name, name) + "        <option value='06'>Buenos Aires</option>\n" +
                "        <option value='10'>Catamarca</option>\n" +
                "        <option value='22'>Chaco</option>\n" +
                "        <option value='26'>Chubut</option>\n" +
                "        <option value='02'>Ciudad Autónoma de Buenos Aires</option>\n" +
                "        <option value='14'>Córdoba</option>\n" +
                "        <option value='18'>Corrientes</option>\n" +
                "        <option value='30'>Entre Ríos</option>\n" +
                "        <option value='34'>Formosa</option>\n" +
                "        <option value='38'>Jujuy</option>\n" +
                "        <option value='42'>La Pampa</option>\n" +
                "        <option value='46'>La Rioja</option>\n" +
                "        <option value='50'>Mendoza</option>\n" +
                "        <option value='54'>Misiones</option>\n" +
                "        <option value='58'>Neuquén</option>\n" +
                "        <option value='62'>Río Negro</option>\n" +
                "        <option value='66'>Salta</option>\n" +
                "        <option value='70'>San Juan</option>\n" +
                "        <option value='74'>San Luis</option>\n" +
                "        <option value='78'>Santa Cruz</option>\n" +
                "        <option value='82'>Santa Fe</option>\n" +
                "        <option value='86'>Santiago del Estero</option>\n" +
                "        <option value='94'>Tierra del Fuego</option>\n" +
                "        <option value='90'>Tucumán</option>\n" +
                "    </select >\n" +
                "</div>";
    }
}

