document.addEventListener('DOMContentLoaded', function () {
	const tabs = document.querySelectorAll('.nav-link')
	const tables = document.querySelectorAll('.report-table')

	tabs.forEach((tab) => {
		tab.addEventListener('click', function (event) {
			event.preventDefault()

			tabs.forEach((t) => t.classList.remove('active'))

			tables.forEach((table) => (table.style.display = 'none'))

			tab.classList.add('active')

			const targetId = tab.getAttribute('data-target')
			document.getElementById(targetId).style.display = 'block'
		})
	})
})
	