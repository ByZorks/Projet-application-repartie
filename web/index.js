import L from 'leaflet';
import urls from './js/env.js';

const map = initMap();

// Handler pour afficher velibs
const boutonLoadVelib = document.getElementById("loadVelib");
boutonLoadVelib.addEventListener("click", (e) => {
    displayVelibs(map)
});

// Handler pour afficher les restaurants
const boutonLoadRestos = document.getElementById("loadRestos");
boutonLoadRestos.addEventListener("click", () => {
    displayRestos(map);
});

// Handler pour les afficher les evenements
const boutonLoadEvents = document.getElementById("loadEvenements");
boutonLoadEvents.addEventListener("click", () => {
    displayEvents(map);
})

/**
 * Initialise la carte Leaflet.
 * @return {L.Map} Retourne une instance de la carte Leaflet centrée sur Nancy.
 */
function initMap() {
    const map = L.map('map').setView([48.691959, 6.184008], 13);

    const tiles = L.tileLayer(urls.TILE_LAYER_URL, {
        maxZoom: 19,
        attribution: "&copy; <a href='${urls.TILE_LAYER_ATTRIBUTION}'>OpenStreetMap</a>"
    }).addTo(map);

    return map;
}

/**
 * Affiche les événements sur la carte.
 * @param map - La carte Leaflet sur laquelle afficher les événements.
 * @returns {Promise<void>}
 */
async function displayEvents(map) {
    const events = await fetchEvents();
    if (events) {
        events.incidents.forEach((event) => {
            const type = event.type;
            const description = event.description;
            const startTime = new Date(event.starttime).toLocaleDateString('fr-FR');
            const endtime = new Date(event.endtime).toLocaleDateString('fr-FR');
            const lat = event.location.polyline.split(' ')[0];
            const lon = event.location.polyline.split(' ')[1];
            const marker = L.marker([lat, lon]).addTo(map);
            marker.bindPopup(`
                <strong>${type}</strong><br>
                Description: ${description}<br>
                Début: ${startTime}<br>
                Fin: ${endtime}<br>
            `);
        })
    }
}

/**
 * Récupère les données des événements depuis l'API.
 * @returns {Promise<any>} Retourne les données des événements
 */
async function fetchEvents() {
    try {
        const response = await fetch(`${urls.API_EVENTS_URL}`);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error("Erreur fetch events: ", error);
    }
}

/**
 * Affiche les marqueurs des restaurants sur la carte.
 * @param map - La carte Leaflet sur laquelle afficher les marqueurs.
 * @returns {Promise<void>}
 */
async function displayRestos(map) {
    const restos = await fetchRestos();
    if (restos) {
        restos.forEach((resto) => {
            const name = resto.nom;
            const address = resto.adresse;
            const lat = resto.latitude;
            const lon = resto.longitude;
            const marker = L.marker([lat, lon]).addTo(map);
            marker.bindPopup(`
                <strong>${name}</strong><br>
                Adresse: ${address}<br>
            `);
        })
    }
}

/**
 * Récupère les données des restaurants depuis l'API.
 * @returns {Promise<any>} Retourne les données des restaurants
 */
async function fetchRestos() {
    try {
        const response = await fetch(`${urls.API_RESTOS_URL}`);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error("Erreur fetch restos: ", error);
    }
}

/**
 * Affiche les marqueurs de velibs sur la carte.
 * @param {L.Map} map - La carte Leaflet sur laquelle afficher les marqueurs.
 * @returns {Promise<void>}
 */
async function displayVelibs(map) {
    const velibs = await fetchVelibs();
    const infostation = await fetchStation();
    if (velibs && infostation) {
        for (let i = 0; i < velibs.data.stations.length; i++) {
            const name = velibs.data.stations[i].name;
            const lat = velibs.data.stations[i].lat;
            const lon = velibs.data.stations[i].lon;
            const address = velibs.data.stations[i].address;
            const capacity = velibs.data.stations[i].capacity;
            const marker = L.marker([lat, lon]).addTo(map);
            let nbvelibdispo = 0;
            nbvelibdispo = infostation.data.stations[i].num_bikes_available;
            marker.bindPopup(`
                <strong>${name}</strong><br>
                Adresse: ${address}<br>
                Capacité: ${capacity}<br>
                Nb velibs dispo: ${nbvelibdispo}<br>
            `);
        }
    }
}

/**
 * Récupère les données des velibs depuis l'API.
 * @returns {Promise<any>} Retourne les données des velibs
 */
async function fetchVelibs() {
    try {
        const response = await fetch(`${urls.API_VELIBS_URL}`);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error("Erreur fetch velibs: ", error);
    }

}

/**
 * Récupère les données des stations depuis l'API
 * @returns {Promise<any>} Retourne les données des stations
 */

async function fetchStation() {
    try {
        const response = await fetch(`${urls.API_STATIONS_URL}`);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.log("Erreur fetch stations : ", error);
    }
}