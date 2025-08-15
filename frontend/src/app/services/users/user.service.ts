import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { UserProfile, UpdateUserProfileRequest, ChangePasswordRequest } from '../../models/responses/user-profile';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMe(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.apiUrl}/users/me`);
  }

  updateMe(payload: UpdateUserProfileRequest): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.apiUrl}/users/me`, payload);
  }

  uploadAvatar(file: File): Observable<UserProfile> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<UserProfile>(`${this.apiUrl}/users/me/avatar`, formData);
  }

  changePassword(payload: ChangePasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/users/me/password`, payload);
  }
}


