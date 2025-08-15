import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DevisRequest } from '../../models/requests/devis/devis-request';
import { DevisResponse } from '../../models/responses/devis/devis-response';
import { GlobalResponse } from '../../models/responses/Auth/GlobalResponse';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DevisService {
  private apiUrl = `${environment.apiUrl}/api/deviss`;

  constructor(private http: HttpClient) {}

  create(request: DevisRequest): Observable<GlobalResponse<DevisResponse>> {
    return this.http.post<GlobalResponse<DevisResponse>>(this.apiUrl, request);
  }

  update(trackingId: string, request: DevisRequest): Observable<GlobalResponse<DevisResponse>> {
    return this.http.put<GlobalResponse<DevisResponse>>(`${this.apiUrl}/${trackingId}`, request);
  }

  delete(trackingId: string): Observable<GlobalResponse<void>> {
    return this.http.delete<GlobalResponse<void>>(`${this.apiUrl}/${trackingId}`);
  }

  findByTrackingId(trackingId: string): Observable<GlobalResponse<DevisResponse>> {
    return this.http.get<GlobalResponse<DevisResponse>>(`${this.apiUrl}/${trackingId}`);
  }

  findAll(): Observable<GlobalResponse<DevisResponse[]>> {
    return this.http.get<GlobalResponse<DevisResponse[]>>(this.apiUrl);
  }

  findByCreatedBy(): Observable<GlobalResponse<DevisResponse[]>> {
    return this.http.get<GlobalResponse<DevisResponse[]>>(`${this.apiUrl}/createdBy`);
  }

  updateDevisStatus(trackingId: string, statut: string): Observable<GlobalResponse<void>> {
    return this.http.put<GlobalResponse<void>>(`${this.apiUrl}/${trackingId}/status`, { statut });
  }

  convertToFacture(trackingId: string): Observable<GlobalResponse<string>> {
    return this.http.post<GlobalResponse<string>>(`${this.apiUrl}/${trackingId}/convertToFacture`, {});
  }

  downloadDevisPDF(trackingId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${trackingId}/downloadPDF`, { responseType: 'blob' });
  }

  getDevisPDF(trackingId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${trackingId}/getPDF`, { responseType: 'blob' });
  }
  
  deleteDevis(trackingId: string): Observable<GlobalResponse<void>> {
    return this.http.delete<GlobalResponse<void>>(`${this.apiUrl}/${trackingId}`);
  }
}


