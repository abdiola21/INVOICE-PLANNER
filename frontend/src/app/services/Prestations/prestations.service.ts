import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { PrestationRequest } from '../../models/requests/Prestations/prestation-request';
import { HttpClient } from '@angular/common/http';
import { PrestationResponse } from '../../models/responses/Prestations/prestation-response';
import { ClientRequest } from '../../models/requests/Clients/client-request';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class PrestationsService {
 /* getClientById(prestationId: string) {
    throw new Error('Method not implemented.');
  }
  updateClient(prestationId: string, clientData: ClientRequest) {
    throw new Error('Method not implemented.');
  }*/
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getPrestations(): Observable<PrestationResponse[]> {
    return this.http.get<any>(`${this.apiUrl}/api/prestations`)
      .pipe(
        map(response => response.data || [])
      );
  }

  getPrestationById(id: string): Observable<PrestationResponse> {
    return this.http.get<any>(`${this.apiUrl}/api/prestations/${id}`)
      .pipe(
        map(response => response.data)
      );
  }

  createPrestation(prestation: PrestationRequest): Observable<PrestationResponse> {
    return this.http.post<any>(`${this.apiUrl}/api/prestations`, prestation)
      .pipe(
        map(response => response.data)
      );
  }

  updatePrestation(id: string, prestation: PrestationRequest): Observable<PrestationResponse> {
    return this.http.put<any>(
      `${this.apiUrl}/api/prestations/${id}`,
      prestation
    ).pipe(
      map(response => response.data)
    );
  }

  deletePrestation(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/api/prestations/${id}`);
  }

  findByCreatedBy(): Observable<PrestationResponse[]> {
    return this.http.get<any>(`${this.apiUrl}/api/prestations/createdBy`)
      .pipe(
        map(response => response.data || [])
      );
  }
}

