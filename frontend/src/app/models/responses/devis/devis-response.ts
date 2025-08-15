import { PrestationResponse } from "../Prestations/prestation-response";

export interface DevisResponse {
    trackingId: string;
    reference: string;
    nomProjet: string;
    description: string;
    clientTrackingId: string;
    clientName: string;
    clientEmail: string;
    createurTrackingId: string;
    createurName: string;
    createurEmail: string;
    dateEmission: Date;
    dateEcheance: Date;
    prixTotal: number;
    prixTTC: number;
    tva: number;
    remise: number;
    statut: string;
    notes: string;
    prestations: PrestationResponse[];
    lignes: any[];
    taxes: any[];
    createdAt: Date;
    updatedAt: Date;
}

