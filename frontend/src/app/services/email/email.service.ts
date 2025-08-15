import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { GlobalResponse } from '../../models/responses/Auth/GlobalResponse';

@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private apiUrl = `${environment.apiUrl}/api/emails`;

  constructor(private http: HttpClient) { }

  /**
   * Envoie un email avec le devis en pièce jointe au client
   * @param devisTrackingId L'identifiant du devis
   * @returns Observable avec la réponse
   */
  sendDevisByEmail(devisTrackingId: string): Observable<GlobalResponse<string>> {
    return this.http.get<GlobalResponse<string>>(`${this.apiUrl}/send-devis/${devisTrackingId}`);
  }
}
