import { ClientResponse } from "../../responses/Clients/client-response";
import { PrestationResponse } from "../../responses/Prestations/prestation-response";

export interface DevisRequest {
    reference: string;
    nomProjet: string;
    description: string;
    client: ClientResponse;
    createur?: string;
    dateEmission: Date;
    dateEcheance: Date;
    prixTotal: number;
    prixTTC: number;
    tva: number;
    remise: number;
    statut: string;
    notes: string;
    prestations: PrestationResponse[];
    lignes ?: string[];
    taxes ?: string[];
}

