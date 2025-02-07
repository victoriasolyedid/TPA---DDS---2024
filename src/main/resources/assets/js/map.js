// Inicializa el mapa y establece su vista en una ubicación específica (latitud, longitud, nivel de zoom)
var map = L.map('map').setView([-36.102, -60.057], 13);

// Añade una capa de mosaicos al mapa (usando mosaicos de OpenStreetMap)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// Añade un marcador al mapa en las coordenadas especificadas
L.marker([-36.102, -60.057]).addTo(map)
  .bindPopup('¡Hola! Soy una heladera.')
  .openPopup();

let marker;

function marcarUbicacion() {
  const colabConDireccion = document.getElementById('tipo_colab')?.value === 'hacerse-cargo-heladera';
  const calle = document.getElementById(colabConDireccion ? "calle_jur" : "calle").value;
  const altura = document.getElementById(colabConDireccion ? "altura_jur" : "altura").value;
  let location = calle + " " + altura;

  fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(location)}`)
    .then(response => response.json())
    .then(data => {
      if (data && data.length > 0) {
        const location = [data[0].lat, data[0].lon];
        if (marker) {
          marker.setLatLng(location);
        } else {
          marker = L.marker(location).addTo(map);
        }
        map.setView(location, 20);
      } else {
        alert('No se encontró la ubicación.');
      }
    });
}