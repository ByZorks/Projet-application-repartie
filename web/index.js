import L from 'leaflet';
import urls from './js/env.js';
import {fetchReservation, fetchTables} from "./js/reservation";

const map = initMap();

// 1ère partie du formulaire
document.getElementById("btnsearch").addEventListener("click", function (e) {
    e.preventDefault(); // important
    const resto = document.getElementById("selectResto").value;
    const date = document.getElementById("dateInput").value;
    const heure = document.getElementById("heureInput").value;
    const nbPersonnes = document.getElementById("nbPersonnesInput").value;

    // Vérification des champs
    if (!resto || !date || !heure || !nbPersonnes) {
        alert("Veuillez remplir tous les champs.");
        return;
    }

    fetchTables(resto, date, heure, nbPersonnes)
        .then(success => {
            if (success) {
                document.getElementById("form1").classList.add("hidden");
                document.getElementById("form2").classList.remove("hidden");
                alert("Réservation effectuée avec succès !");
            } else {
                alert("Erreur lors de la recherche de tables");
            }
        })
        .catch(err => {
            alert("Erreur de communication avec le service")
            console.log("Erreur",err);
        });
});

// 2ème partie du formulaire
document.getElementById("btnsave").addEventListener("click", function (e) {
    e.preventDefault(); // important
    const nom = document.getElementById("nomInput").value;
    const p = document.getElementById("prenomInput").value;
    const t = document.getElementById("telInput").value;

    // Vérification des champs
    if (!nom || !p || !t) {
        alert("Veuillez remplir tous les champs.");
        return;
    }

    fetchReservation(nom, p, t)
        .then(success => {
            if (success) {
                document.getElementById("form1").classList.remove("hidden");
                document.getElementById("form2").classList.add("hidden");
                alert("Réservation effectuée avec succès !");
            } else {
                alert("Erreur lors de la réservation");

            }
        })
        .catch(err => {
            alert("Erreur de communication avec le service")
            console.log("Erreur",err);
        });
});

// Handler pour afficher velibs
const boutonLoadVelib = document.getElementById("loadVelib");
boutonLoadVelib.addEventListener("click", () => {
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

// Ajoute les restaurants dans le select
const selectResto = document.getElementById("selectResto");
fetchRestos().then((restos) => {
    if (restos) {
        restos.forEach((resto) => {
            const option = document.createElement("option");
            option.value = resto.id;
            option.textContent = resto.nom;
            selectResto.appendChild(option);
        });
    }
}).catch((error) => {
    console.error("Erreur lors de la récupération des restaurants: ", error);
});

/**
 * Initialise la carte Leaflet.
 * @return {L.Map} Retourne une instance de la carte Leaflet centrée sur Nancy.
 */
function initMap() {
    const map = L.map('map').setView([48.691959, 6.184008], 13);
    L.tileLayer(urls.TILE_LAYER_URL, {
        maxZoom: 19,
        attribution: `&copy; <a href='${urls.TILE_LAYER_ATTRIBUTION}'>OpenStreetMap</a>̀`
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
        const redMarker = L.icon({
            iconUrl: 'images/marker-icon-2x-rouge.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowUrl: 'node_modules/leaflet/dist/images/marker-shadow.png',
            shadowSize: [41, 41],
            shadowAnchor: [12, 41]
        });

        events.incidents.forEach((event) => {
            const type = event.type;
            const description = event.description;
            const startTime = new Date(event.starttime).toLocaleDateString('fr-FR');
            const endtime = new Date(event.endtime).toLocaleDateString('fr-FR');
            const lat = event.location.polyline.split(' ')[0];
            const lon = event.location.polyline.split(' ')[1];
            const marker = L.marker([lat, lon], {icon: redMarker}).addTo(map);
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
        alert("Erreur de communication avec le service")
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
        const greenMarker = L.icon({
            iconUrl: 'images/marker-icon-2x-vert.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowUrl: 'node_modules/leaflet/dist/images/marker-shadow.png',
            shadowSize: [41, 41],
            shadowAnchor: [12, 41]
        });

        restos.forEach((resto) => {
            const name = resto.nom;
            const address = resto.adresse;
            const lat = resto.latitude;
            const lon = resto.longitude;
            const marker = L.marker([lat, lon], {icon: greenMarker}).addTo(map);
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
        alert("Erreur de communication avec le service")
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
        alert("Erreur de communication avec le service")
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
        alert("Erreur de communication avec le service")
        console.log("Erreur fetch stations : ", error);
    }
}