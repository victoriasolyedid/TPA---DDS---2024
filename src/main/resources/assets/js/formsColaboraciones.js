function mostrarCamposHumana() {
    var selectedOption = document.getElementById('tipo_colab').value
    var donacionDineroFields = document.getElementById('donacionDineroFieldsHum')
    var donacionViandaFields = document.getElementById('donacionViandaFieldsHum')
    var distribucionViandasFields = document.getElementById('distribucionViandasFieldsHum')
    var altaPersonaFields = document.getElementById('altaPersonaFieldsHum')

    donacionDineroFields.style.display = 'none'
    donacionViandaFields.style.display = 'none'
    distribucionViandasFields.style.display = 'none'
    altaPersonaFields.style.display = 'none'

    if (selectedOption === 'donacion-dinero') {
        donacionDineroFields.style.display = 'contents'
    } else if (selectedOption === 'donacion-vianda') {
        donacionViandaFields.style.display = 'contents'
    } else if (selectedOption === 'distribucion-viandas') {
        distribucionViandasFields.style.display = 'contents'
    } else if (selectedOption === 'alta-persona') {
        altaPersonaFields.style.display = 'contents'
    }
}

function mostrarCamposJuridica() {
    var selectedOption = document.getElementById('tipo_colab').value
    var donacionDineroField = document.getElementById('donacionDineroFieldJur')
    var donacionDineroField2 = document.getElementById('donacionDineroFieldJur2')
    var hacerseCargoHeladera = document.getElementById('hacerseCargoHeladeraFieldsJur')

    donacionDineroField.style.display = 'none'
    donacionDineroField2.style.display = 'none'
    hacerseCargoHeladera.style.display = 'none'

    if (selectedOption === 'donacion-dinero') {
        donacionDineroField.style.display = 'inline-block'
        donacionDineroField2.style.display = 'inline-block'
    } else if (selectedOption === 'hacerse-cargo-heladera') {
        hacerseCargoHeladera.style.display = 'flex'
    }
}

if (tipoPersona === "JURIDICA") {
    document.getElementById('nuevaColabFormJuridica').addEventListener('submit', function (event) {
        const invalidFields = [
            'calle_heladera', 'altura_heladera', 'fecha_funcionamiento_heladera',
            'nombre_heladera', 'capacidad_heladera', 'razon_social', 'rubro',
            'email_jur', 'whatsapp_jur', 'telefono_jur', 'calle_jur', 'altura_jur',
            'localidad_jur', 'provincia_jur'
        ];
        invalidFields.forEach(function (fieldId) {
            var field = document.getElementById(fieldId);
            if (field && (field.style.display === 'none' || field.readOnly)) {
                field.disabled = true;
            }
        });
    });
} else {
    document.getElementById('nuevaColabFormHumana').addEventListener('submit', function (event) {
        var invalidFields = [
            'nombre', 'apellido', 'email', 'whatsapp', 'telefono', 'fecha_nacimiento',
            'calle', 'altura', 'provincia', 'localidad', 'monto_donacion', 'frecuencia_donacion',
            'comida_donacion', 'vto_comida_donacion', 'comida_heladera_donacion', 'distribucion_heladera_origen',
            'distribucion_heladera_destino', 'cantidad_distribucion', 'motivo_distribucion', 'nombre_vulnerable',
            'apellido_vulnerable', 'tipo_doc_vulnerable', 'num_doc_vulnerable', 'fecha_registro_vulnerable',
            'fecha_nacimiento_vulnerable', 'calle_vulnerable', 'altura_vulnerable', 'provincia_vulnerable',
            'localidad_vulnerable', 'cant_menores_vulnerable'
        ];
        invalidFields.forEach(function (fieldId) {
            var field = document.getElementById(fieldId);
            if (field && (field.style.display === 'none' || field.readOnly)) {
                field.disabled = true;
            }
        });
    });
}