import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CompanyProfile } from '../../models/requests/CompanyProfile/CompanyProfile';
import { GlobalResponse } from '../../models/responses/Auth/GlobalResponse';



@Injectable({
  providedIn: 'root'
})
export class CompletionProfileService {
  private apiUrl = `${environment.apiUrl}/api/profile`;

  constructor(private http: HttpClient) { }

  getProfile(): Observable<CompanyProfile> {
    return this.http.get<CompanyProfile>(this.apiUrl);
  }

  isProfileComplete(): Observable<GlobalResponse<boolean>> {
    return this.http.get<GlobalResponse<boolean>>(`${this.apiUrl}/status`);
  }

  createOrUpdateProfile(profileData: Partial<CompanyProfile>): Observable<CompanyProfile> {
    return this.http.post<CompanyProfile>(this.apiUrl, profileData);
  }

  uploadLogo(file: File): Observable<CompanyProfile> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<CompanyProfile>(`${this.apiUrl}/logo`, formData);
  }

  getLogoImage(profileId: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/logo/${encodeURIComponent(profileId)}`, { responseType: 'blob' });
  }
}

