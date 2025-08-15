export interface FactureResponse {
    trackingId: string;
    numero: string;
    clientTrackingId: string;
    clientName: string;
    dateEcheance: Date;
    montantHT: number;
    montantTTC: number;
    remise: number;
    etat: string;
    modeReglement: string;
    referenceDevis: string;
    createdAt: Date;
    updatedAt: Date;
    prestations?: PrestationFacture[];
}

export interface PrestationFacture {
    id: number;
    description: string;
    quantite: number;
    prixUnitaire: number;
    prixTotal: number;
}

