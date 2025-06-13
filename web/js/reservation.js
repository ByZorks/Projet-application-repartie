import urls from "./env.js";

let idresto = -1;
let date = "";
let heure = "";
let nbP = 0;

/**
 * Récupère les tables disponibles pour un restaurant à une date et heure données
 * @param resto Restaurant ID
 * @param d Date
 * @param h Heure
 * @param nb Nombre de personnes
 * @returns {Promise<boolean>}
 */
export async function fetchTables(resto, d, h, nb) {
    const data = {
        restaurantId: resto,
        date: d,
        heure: h,
        nbPersonnes: nb
    };

    idresto = resto;
    date = d;
    heure = h;
    nbP = nb;

    console.log("fetchTables called with data:", data);

    try {
        console.log(urls.API_RESTOS_URL);
        const response = await fetch(urls.API_RESTOS_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const text = await response.text();

        console.log("Response text:", text);

        let result;
        try {
            result = JSON.parse(text);
        } catch (parseError) {
            console.error("Erreur de parsing JSON:", parseError);
            return false;
        }

        if (result.message) {
            alert(result.message);
            return true;
        } else if (result.error) {
            alert("Erreur : " + result.error);
            return false;
        }
        return false;
    } catch (error) {
        console.error("Erreur lors de la récupération des tables :", error);
        return false;
    }
}

/**
 * Envoie une réservation pour un restaurant à une date et heure données
 * @param nn Nom du client
 * @param p Prénom du client
 * @param t Téléphone du client
 * @returns {Promise<boolean>}
 */
export async function fetchReservation(nn, p, t) {
    const data = {
        nom: nn,
        prenom: p,
        telephone: t,
        restaurantId: idresto,
        date: date,
        heure: heure,
        nbPersonnes: nbP,
    };

    try {
        const response = await fetch(urls.API_RESTOS_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const text = await response.text();

        console.log("Response text:", text);

        let result;
        try {
            result = JSON.parse(text);
        } catch (parseError) {
            console.error("Erreur de parsing JSON:", parseError);
            return false;
        }

        if (result.message) {
            alert(result.message);
            return true;
        } else if (result.error) {
            alert("Erreur : " + result.error);
            return false;
        }
        return false;
    } catch (error) {
        console.error("Erreur lors de la récupération des tables :", error);
        return false;
    }
}
