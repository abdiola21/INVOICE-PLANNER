import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest } from '../../models/requests/Auth/login-request';
import { RegisterRequest } from '../../models/requests/Auth/register-request';
import { LoginResponse } from '../../models/responses/Auth/login-response';
import { GlobalResponse } from '../../models/responses/Auth/GlobalResponse';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<LoginResponse | null>;
  public currentUser: Observable<LoginResponse | null>;
  private apiUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const storedUser = localStorage.getItem('currentUser');
    try {
      this.currentUserSubject = new BehaviorSubject<LoginResponse | null>(
        storedUser ? JSON.parse(storedUser) : null
      );
    } catch (error) {
      console.error('Erreur d\'analyse JSON dans localStorage:', error);
      localStorage.removeItem('currentUser');
      this.currentUserSubject = new BehaviorSubject<LoginResponse | null>(null);
    }
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  login(loginData: LoginRequest): Observable<GlobalResponse<LoginResponse>> {
    return this.http.post<GlobalResponse<LoginResponse>>(`${this.apiUrl}/auth/login`, loginData)
      .pipe(
        tap(response => {
          if (!response.error && response.data) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            this.currentUserSubject.next(response.data);
          }
        })
      );
  }

  register(registerData: RegisterRequest): Observable<GlobalResponse<any>> {
    return this.http.post<GlobalResponse<any>>(`${this.apiUrl}/auth/register`, registerData);
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth']);
  }

  refreshToken(): Observable<GlobalResponse<LoginResponse>> {
    const refreshToken = this.currentUserValue?.refreshToken;

    if (!refreshToken) {
      return throwError(() => new Error('Refresh token not available'));
    }

    return this.http.post<GlobalResponse<LoginResponse>>(`${this.apiUrl}/auth/refresh-token`, { refreshToken })
      .pipe(
        tap(response => {
          if (!response.error && response.data) {
            localStorage.setItem('currentUser', JSON.stringify(response.data));
            this.currentUserSubject.next(response.data);
          }
        }),
        catchError(error => {
          this.logout();
          return throwError(() => error);
        })
      );
  }

  isAuthenticated(): boolean {
    return this.currentUserValue !== null;
  }
}

