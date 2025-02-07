// Eventos para persona jurídica
const editarPerfilJuridica = document.getElementById('editar-perfil-juridica');
const guardarCambiosJuridica = document.getElementById('guardar-cambios-juridica');
const cancelarCambiosJuridica = document.getElementById('cancelar-cambios-juridica');

editarPerfilJuridica.addEventListener('click', function() {
    let telefono = document.getElementById('telefono');
    let direccion = document.getElementById('direccion');
    telefono.removeAttribute('readonly');
    direccion.removeAttribute('readonly');
    this.style.display = 'none'; 
    
    guardarCambiosJuridica.style.display = 'block'; 
    cancelarCambiosJuridica.style.display = 'block'; 
});

cancelarCambiosJuridica.addEventListener('click', function() {
    let telefono = document.getElementById('telefono');
    let direccion = document.getElementById('direccion');
    telefono.setAttribute('readonly', true);
    direccion.setAttribute('readonly', true);

    editarPerfilJuridica.style.display = 'block'; 
    guardarCambiosJuridica.style.display = 'none'; 
    cancelarCambiosJuridica.style.display = 'none'; 
});