document.addEventListener('DOMContentLoaded', function () {
    // Seleccionar todas las pestañas y contenido
    const tabs = document.querySelectorAll('#nav-tabs2 .nav-link');
    const tabContents = document.querySelectorAll('.tab-content');

    // Añadir un evento de clic a cada pestaña
    tabs.forEach(tab => {
        tab.addEventListener('click', function (event) {
            event.preventDefault();
            
            // Obtener el id de la sección objetivo desde el atributo 'data-target'
            const targetId = this.getAttribute('data-target');
            const targetContent = document.getElementById(targetId);

            // Eliminar la clase 'active' de todas las pestañas y contenidos
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(tc => tc.style.display = 'none');

            // Agregar la clase 'active' a la pestaña clickeada y mostrar el contenido objetivo
            this.classList.add('active');
            if (targetContent) {
                targetContent.style.display = 'block';
            }
        });
    });
});
