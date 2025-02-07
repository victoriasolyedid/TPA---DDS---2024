// Eventos para persona humana
const editarPerfilHumana = document.getElementById('editar-perfil');
editarPerfilHumana.addEventListener('click', function() {
    let telefono = document.getElementById('telefono');
    let direccion = document.getElementById('direccion');
    telefono.removeAttribute('readonly');
    direccion.removeAttribute('readonly');
    fechaNacimiento.removeAttribute('readonly');
    this.style.display = 'none'; 
    document.getElementById('guardar-cambios').style.display = 'block'; 
    document.getElementById('cancelar-cambios').style.display = 'block'; 
});

const cancelarCambiosHumana = document.getElementById('cancelar-cambios');
cancelarCambiosHumana.addEventListener('click', function() {
    let telefono = document.getElementById('user.telefono1');
    let direccion = document.getElementById('user.direccion1');;
    telefono.setAttribute('readonly', true);
    direccion.setAttribute('readonly', true);
    fechaNacimiento.setAttribute('readonly', true);
    document.getElementById('editar-perfil').style.display = 'block'; 
    document.getElementById('guardar-cambios').style.display = 'none'; 
    document.getElementById('cancelar-cambios').style.display = 'none'; 
});

// Eventos para persona jurídica
const editarPerfilJuridica = document.getElementById('editar-perfil-juridica');
editarPerfilJuridica.addEventListener('click', function() {
    let telefono = document.getElementById('user.telefono');
    let direccion = document.getElementById('user.direccion');
    telefono.removeAttribute('readonly');
    direccion.removeAttribute('readonly');
    this.style.display = 'none'; 
    document.getElementById('guardar-cambios-juridica').style.display = 'block'; 
    document.getElementById('cancelar-cambios-juridica').style.display = 'block'; 
});

const cancelarCambiosJuridica = document.getElementById('cancelar-cambios-juridica');
cancelarCambiosJuridica.addEventListener('click', function() {
    let telefono = document.getElementById('user.telefono');
    let direccion = document.getElementById('user.direccion');
    telefono.setAttribute('readonly', true);
    direccion.setAttribute('readonly', true);
    document.getElementById('editar-perfil-juridica').style.display = 'block'; 
    document.getElementById('guardar-cambios-juridica').style.display = 'none'; 
    document.getElementById('cancelar-cambios-juridica').style.display = 'none'; 
});