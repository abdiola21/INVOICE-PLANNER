import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { GlobalResponse } from '../../models/responses/Auth/GlobalResponse';
import { FactureResponse } from '../../models/responses/facture/facture-response';

/** Payload attendu par le backend pour créer une facture manuelle */
export interface FactureCreatePayload {
  clientId: number;                          // id numérique du client
  referenceDevis?: string | null;
  dateEcheance: string;                      // 'yyyy-MM-dd' (LocalDate côté back)
  modeReglement: 'ESPECES' | 'VIREMENT' | 'CHEQUE' | 'CARTE' | string;
  etat: 'BROUILLON' | 'ENVOYE' | 'PAYE' | 'RETARD' | 'ANNULEE';
  remise?: number;
  montantHt: number;
  montantTtc: number;
  lignes?: Array<{
    designation: string;
    prixUnitaire: number;
    quantite: number;
  }>;
}

@Injectable({ providedIn: 'root' })
export class FactureService {
  private apiUrl = `${environment.apiUrl}/api/factures`;
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {}

  findAll(): Observable<GlobalResponse<FactureResponse[]>> {
    return this.http.get<GlobalResponse<FactureResponse[]>>(this.apiUrl);
  }

  findByTrackingId(trackingId: string): Observable<GlobalResponse<FactureResponse>> {
    return this.http.get<GlobalResponse<FactureResponse>>(`${this.apiUrl}/${trackingId}`);
  }

  /** Conversion d’un devis en facture (endpoint actuel côté back) */
  createFromDevis(trackingId: string): Observable<GlobalResponse<FactureResponse>> {
    return this.http.get<GlobalResponse<FactureResponse>>(`${this.apiUrl}/save/${trackingId}`);
  }

  updateFacture(facture: any): Observable<GlobalResponse<FactureResponse>> {
    return this.http.put<GlobalResponse<FactureResponse>>(
      `${this.apiUrl}/${facture.trackingId}`,
      facture,
      this.httpOptions
    );
  }

  /** Création manuelle d’une facture */
  createFacture(payload: any) {
    return this.http.post<GlobalResponse<FactureResponse>>(this.apiUrl, payload);
  }


  updateStatus(trackingId: string, statut: string): Observable<GlobalResponse<FactureResponse>> {
    // si le back attend "etat" à la place de "statut", on adaptera ici
    return this.http.put<GlobalResponse<FactureResponse>>(
      `${this.apiUrl}/${trackingId}/status`,
      { statut },
      this.httpOptions
    );
  }
}

