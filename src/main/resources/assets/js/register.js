function mostrarCampos() {
	var selectedOption = document.getElementById('tipo_usuario').value
	var humanoFields = document.getElementById('alta-personaHum-fields-hum')
	var juridicaFields = document.getElementById('alta-personaJur-fields-jur')

	humanoFields.style.display = 'none'
	juridicaFields.style.display = 'none'

	if (selectedOption === 'humana') {
		humanoFields.style.display = 'contents'
	} else if (selectedOption === 'juridica') {
		juridicaFields.style.display = 'contents'
	}
}