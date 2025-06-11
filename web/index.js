import L from 'leaflet';
require('dotenv').config();

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

    const tiles = L.tileLayer(process.env.TILE_LAYER_URL, {
        maxZoom: 19,
        attribution: "&copy; <a href='${process.env.TILE_LAYER_ATTRIBUTION}'>OpenStreetMap</a>"
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
    const infostation = await fetchStation();
    if(velibs && infostation) {
        for(let i = 0;i<velibs.data.stations.length;i++){
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
        const response = await fetch(`${procress.env.API_VELIBS_URL}`);
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

async function fetchStation(){
    try{
        const response = await fetch(`${process.env.API_STATIONS_URL}`);
        if(response.ok){
            return await response.json();
        }
    }catch (error){
        console.log("Erreur fetch stations : ",error);
    }
}