// Para que el mapa funcione OK, agrega un evento input a los campos de calle y altura
// para verificar si ambos están completos. Si es así,
// habilita el campo de selección de la provincia; de lo contrario, lo deshabilita.

let cachedLocalidades = {};
document.addEventListener('DOMContentLoaded', () => {
    precargarLocalidades();

    const tipoPersona = document.getElementById('tipo_usuario');
    if (tipoPersona) {
        tipoPersona.addEventListener('change', () => {
            escucharCalleAltura();
        });
    }

    function checkInputs() {
        const isJuridica = tipoPersona.value === "JURIDICA";
        const calleInput = document.getElementById(isJuridica ? 'calle_jur' : 'calle');
        const alturaInput = document.getElementById(isJuridica ? 'altura_jur' : 'altura');
        const provinciaSelect = document.getElementById(isJuridica ? 'provincia_jur' : 'provincia');
        const localidadesSelect = document.getElementById(isJuridica ? 'localidad_jur' : 'localidad');

        if (provinciaSelect && localidadesSelect) {
            provinciaSelect.disabled = !(calleInput.value.trim() !== '' && alturaInput.value.trim() !== '');
            localidadesSelect.disabled = provinciaSelect.disabled || provinciaSelect.value === "Seleccionar Provincia";
        } else {
            console.error('No se encontró el campo de selección de provincia');
        }
    }

    function escucharCalleAltura() {
        const isJuridica = tipoPersona?.value === "JURIDICA";
        const colabConDireccion = document.getElementById('tipo_colab')?.value === 'hacerse-cargo-heladera';
        const calleInput = document.getElementById(isJuridica || colabConDireccion ? 'calle_jur' : 'calle');
        const alturaInput = document.getElementById(isJuridica || colabConDireccion ? 'altura_jur' : 'altura');

        if (calleInput && alturaInput) {
            calleInput.addEventListener('input', checkInputs);
            alturaInput.addEventListener('input', checkInputs);
        }
    }

    escucharCalleAltura();
});

async function cargarLocalidades(event) {
    const provinciaInput = event.target;
    const provinciaId = provinciaInput.value;

    if (provinciaId === "Seleccionar Provincia") {
        console.error('No se seleccionó ninguna provincia');
        return;
    }

    const localidadesSelect = provinciaInput.id === "provincia" ? document.getElementById('localidad') : document.getElementById('localidad_jur');
    localidadesSelect.innerHTML = '<option selected>Seleccionar Localidad</option>';
    localidadesSelect.disabled = true;

    if (cachedLocalidades[provinciaId]) {
        populateLocalidades(cachedLocalidades[provinciaId], localidadesSelect);
        localidadesSelect.disabled = false;
    } else {
        console.error(`No se encontraron localidades en la caché para la provincia ${provinciaId}`);
    }
}

function populateLocalidades(localidades, localidadesSelect) {
    const fragment = document.createDocumentFragment();

    localidades.forEach(localidad => {
        const option = document.createElement('option');
        option.value = localidad.id;
        option.textContent = localidad.nombre;
        fragment.appendChild(option);
    });

    localidadesSelect.appendChild(fragment);
}

async function precargarLocalidades() {
    const provincias = ['02', '06', '10', '14', '18', '22', '26', '30', '34', '38', '42', '46', '50', '54', '58', '62', '66', '70', '74', '78', '82', '86', '90', '94'];
    const fetchPromises = provincias.map(provinciaId => {
        let url;
        if (provinciaId === '02' || provinciaId === '86') {
            url = `https://apis.datos.gob.ar/georef/api/localidades?provincia=${provinciaId}&max=150&orden=nombre`;
        } else {
            url = `https://apis.datos.gob.ar/georef/api/municipios?provincia=${provinciaId}&max=150&orden=nombre`;
        }

        return fetch(url)
            .then(response => response.json())
            .then(data => {
                cachedLocalidades[provinciaId] = provinciaId === '02' || provinciaId === '86' ? data.localidades : data.municipios;
            })
            .catch(error => {
                console.error(`Error al precargar las localidades para la provincia ${provinciaId}:`, error);
            });
    });

    await Promise.all(fetchPromises);
}

/* async function getNombreLocalidad(localidadId) {
    if (localidadId === "Seleccionar Localidad") {
        console.error('No se seleccionó ninguna localidad');
        return;
    }

    const urlLocalidades = `https://apis.datos.gob.ar/georef/api/localidades?id=${localidadId}&aplanar=true&exacto=true`;
    const urlMunicipios = `https://apis.datos.gob.ar/georef/api/municipios?id=${localidadId}&aplanar=true&exacto=true`;

    let localidad = await fetch(urlLocalidades)
        .then(response => response.json())
        .then(data => data.localidades && data.localidades.length > 0 ? data.localidades[0] : null)
        .catch(error => {
            console.error(`Error al buscar la localidad con ID ${localidadId}:`, error);
            return null;
        });

    if (!localidad) {
        localidad = await fetch(urlMunicipios)
            .then(response => response.json())
            .then(data => data.municipios && data.municipios.length > 0 ? data.municipios[0] : null)
            .catch(error => {
                console.error(`Error al buscar el municipio con ID ${localidadId}:`, error);
                return null;
            });
    }

    if (localidad) {
        return localidad.nombre;
    } else {
        return "Desconocida";
    }
} */