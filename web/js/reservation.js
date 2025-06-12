import urls from "./env";

let idtable = -1;
let idresto = -1;
let date = "";
let heure = "";





export async function fetchTables(resto, d, h) {
    const data = {
        restaurantId: resto,
        date: d,
        heure: h,
    };

    idresto = resto;
    date = d;
    heure = h;

    try {
        const response = await fetch("http://localhost:8000/BD", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        const nombre = await response.text();
        idtable = Number(nombre);
        console.log("Nombre reçu :", idtable);
        if (idtable === -1) {
            alert("Aucune table disponible pour cette date et heure.");
            return false;
        }
        return true;
    } catch (error) {
        console.error("Erreur lors de la récupération des tables: ", error);
        return false;
    }
}


export async function fetchReservation(nn, p, t, nb) {
    const data = {
        nom: nn,
        prenom: p,
        telephone: t,
        restaurantId: idresto,
        date: date,
        heure: heure,
        nbPersonnes: nb,
        table : idtable
    };

    try {
        const response = await fetch("http://localhost:8000/BD", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            console.error("Erreur HTTP:", response.status);
            return false;
        }

        return true;
    } catch (error) {
        console.error("Erreur lors de l'envoi de la réservation:", error);
        return false;
    }
}
