import L from 'leaflet';

const map = initMap();
const boutonLoadVelib = document.getElementById("loadVelib");
boutonLoadVelib.addEventListener("click",(e)=>{
    displayVelibs(map)
});

/**
 * Initialise la carte Leaflet.
 * @return {L.Map} Retourne une instance de la carte Leaflet centrée sur Nancy.
 */
function initMap() {
    const map = L.map('map').setView([48.691959, 6.184008], 13);

    const tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    return map;
}

/**
 * Affiche les marqueurs de velibs sur la carte.
 * @param {L.Map} map - La carte Leaflet sur laquelle afficher les marqueurs.
 * @returns {Promise<void>}
 */
async function displayVelibs(map) {
    const velibs = await fetchVelibs();
    if(velibs) {
        velibs.data.stations.forEach((velib) => {
            const name = velib.name;
            const lat = velib.lat;
            const lon = velib.lon;
            const address = velib.address;
            const capacity = velib.capacity;
            const marker = L.marker([lat, lon]).addTo(map);
            marker.bindPopup(`
            <strong>${name}</strong><br>
            Adresse: ${address}<br>
            Capacité: ${capacity}<br>
        `);
        })
    }
}

/**
 * Récupère les données des velibs depuis l'API.
 * @returns {Promise<any>} Rétourne les données des velibs
 */
async function fetchVelibs() {
    try {
        const response = await fetch("https://api.cyclocity.fr/contracts/nancy/gbfs/v2/station_information.json");
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error("Erreur fetch velibs: ", error);
    }

}