// Eventos para persona humana
const editarPerfilHumana = document.getElementById('editar-perfil');
const guardarCambiosHumana = document.getElementById('guardar-cambios');
const cancelarCambiosHumana = document.getElementById('cancelar-cambios');

editarPerfilHumana.addEventListener('click', function() {
    let telefono = document.getElementById('telefono');
    let direccion = document.getElementById('direccion');
    telefono.removeAttribute('readonly');
    direccion.removeAttribute('readonly');
    this.style.display = 'none'; 

    guardarCambiosHumana.style.display = 'block'; 
    cancelarCambiosHumana.style.display = 'block'; 
});

cancelarCambiosHumana.addEventListener('click', function() {
    let telefono = document.getElementById('telefono');
    let direccion = document.getElementById('direccion');
    telefono.setAttribute('readonly', true);
    direccion.setAttribute('readonly', true);

    editarPerfilHumana.style.display = 'block';
    guardarCambiosHumana.style.display = 'none'; 
    cancelarCambiosHumana.style.display = 'none'; 
});


/* document.addEventListener('DOMContentLoaded', function() {
    // Eventos para persona humana
    document.getElementById('editar-perfil').addEventListener('click', function() {
        let telefono = document.getElementById('user.telefono1');
        let direccion = document.getElementById('user.direccion1');
        let fechaNacimiento = document.getElementById('user.fecha_nacimiento');
        telefono.removeAttribute('readonly');
        direccion.removeAttribute('readonly');
        fechaNacimiento.removeAttribute('readonly');
        this.style.display = 'none'; 
        document.getElementById('guardar-cambios').style.display = 'block'; 
        document.getElementById('cancelar-cambios').style.display = 'block'; 
    });

    document.getElementById('guardar-cambios').addEventListener('click', function() {
        alert('Información guardada correctamente');
    });

    document.getElementById('cancelar-cambios').addEventListener('click', function() {
        let telefono = document.getElementById('user.telefono1');
        let direccion = document.getElementById('user.direccion1');
        let fechaNacimiento = document.getElementById('user.fecha_nacimiento');
        telefono.setAttribute('readonly', true);
        direccion.setAttribute('readonly', true);
        fechaNacimiento.setAttribute('readonly', true);
        document.getElementById('editar-perfil').style.display = 'block'; 
        document.getElementById('guardar-cambios').style.display = 'none'; 
        document.getElementById('cancelar-cambios').style.display = 'none'; 
    });

    // Eventos para persona jurídica
    document.getElementById('editar-perfil-juridica').addEventListener('click', function() {
        let telefono = document.getElementById('user.telefono');
        let direccion = document.getElementById('user.direccion');
        telefono.removeAttribute('readonly');
        direccion.removeAttribute('readonly');
        this.style.display = 'none'; 
        document.getElementById('guardar-cambios-juridica').style.display = 'block'; 
        document.getElementById('cancelar-cambios-juridica').style.display = 'block'; 
    });

    document.getElementById('guardar-cambios-juridica').addEventListener('click', function() {
        alert('Información guardada correctamente');
    });

    document.getElementById('cancelar-cambios-juridica').addEventListener('click', function() {
        let telefono = document.getElementById('user.telefono');
        let direccion = document.getElementById('user.direccion');
        telefono.setAttribute('readonly', true);
        direccion.setAttribute('readonly', true);
        document.getElementById('editar-perfil-juridica').style.display = 'block'; 
        document.getElementById('guardar-cambios-juridica').style.display = 'none'; 
        document.getElementById('cancelar-cambios-juridica').style.display = 'none'; 
    });
}); */