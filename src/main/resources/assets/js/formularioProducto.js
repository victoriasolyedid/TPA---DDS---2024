document.addEventListener("DOMContentLoaded", function () {
    var miModal = document.getElementById('miModal');
    var form = document.querySelector('form[action="/canjear-producto"]');

    if (miModal && form) {

        miModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget; // Botón que activó el modal

            var productoId = button.getAttribute('data-id');
            var valorPuntos = button.getAttribute('data-puntos');

            console.log("Producto ID (al abrir modal):", productoId);
            console.log("Puntos (al abrir modal):", valorPuntos);

            document.getElementById('productoId').value = productoId;
            document.getElementById('puntos').value = valorPuntos;

            console.log("Producto ID en input (modal abierto):", document.getElementById('productoId').value);
            console.log("Puntos en input (modal abierto):", document.getElementById('puntos').value);
        });

        form.addEventListener('submit', function (event) {
            var productoId = document.getElementById('productoId').value;
            var puntos = document.getElementById('puntos').value;

            console.log("Producto ID en form submit (antes de envío):", productoId);
            console.log("Puntos en form submit (antes de envío):", puntos);

            if (!productoId || !puntos) {
                event.preventDefault();
                alert("Faltan datos, no se puede enviar el formulario.");
            }
        });
    } else {
        console.error("Modal o formulario no encontrados en el DOM.");
    }
});
