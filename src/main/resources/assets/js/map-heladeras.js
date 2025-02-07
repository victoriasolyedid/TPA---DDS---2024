// Inicializa el mapa y establece su vista en una ubicación específica (latitud, longitud, nivel de zoom)
var map = L.map('map').setView([-35.902, -59.857], 7);

// Añade una capa de mosaicos al mapa (usando mosaicos de OpenStreetMap)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

var greenIcon = new L.Icon({
    iconUrl: 'https://icones.pro/wp-content/uploads/2021/02/icone-de-broche-de-localisation-verte.png',
    iconSize: [45, 45],
    iconAnchor: [22, 45],
    popupAnchor: [1, -30],
});

var redIcon = new L.Icon({
    iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
    iconSize: [33, 35],
    iconAnchor: [22, 45],
    popupAnchor: [1, -30],
});

function marcarHeladeras() {
    todasLasHeladeras.forEach(heladera => {
        let ubicacion = heladera.ubicacion;
        let direccion = ubicacion.calle + " " + ubicacion.altura + ", " + ubicacion.localidad.localidad + ", " + ubicacion.provincia.provincia;
        let icon = heladera.activa ? greenIcon : redIcon;
        let estado = heladera.activa ? "activa" : "inactiva";

        fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccion)}`)
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    const location = [data[0].lat, data[0].lon];
                    let marker = L.marker(location, { icon: icon }).addTo(map);
                    marker.bindPopup(`<b>${heladera.nombre}</b><br>Ubicación: ${direccion}<br>Estado: ${estado}<br>Viandas: ${heladera.cantViandas}`).openPopup();
                } else {
                    alert('No se encontró la ubicación.');
                }
            });
    });
}