import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { ClientResponse } from '../../models/responses/Clients/client-response';
import { ClientRequest } from '../../models/requests/Clients/client-request';

interface ApiResponse<T> {
  data: T;
  message: string;
  success: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ClientsService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getClients(): Observable<ClientResponse[]> {
    return this.http.get<ApiResponse<ClientResponse[]>>(`${this.apiUrl}/api/clients/createdBy`)
      .pipe(map(response => response.data || []));
  }

  getClientById(id: string): Observable<ClientResponse> {
    return this.http.get<ApiResponse<ClientResponse>>(`${this.apiUrl}/api/clients/${id}`)
      .pipe(map(response => response.data));
  }

  createClient(client: ClientRequest): Observable<ClientResponse> {
    return this.http.post<ApiResponse<ClientResponse>>(`${this.apiUrl}/api/clients`, client)
      .pipe(map(response => response.data));
  }
  
  // Alias pour createClient pour une meilleure intégration avec les composants
  create(client: any): Observable<ClientResponse> {
    return this.createClient(client);
  }

  updateClient(id: string, client: ClientRequest): Observable<ClientResponse> {
    return this.http.put<ApiResponse<ClientResponse>>(
      `${this.apiUrl}/api/clients/${id}`,
      client
    ).pipe(map(response => response.data));
  }

  deleteClient(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/api/clients/${id}`);
  }
}

