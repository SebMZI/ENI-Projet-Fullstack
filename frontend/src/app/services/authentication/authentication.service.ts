import { inject, Injectable } from '@angular/core';
import { AuthRequest } from '../../interfaces/authRequest';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../../environments/environment';
import { Utilisateur } from '../../interfaces/utilisateur';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthResponse } from '../../interfaces/auth-response';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private userSubject = new BehaviorSubject<Utilisateur | null>(null);
  user$ = this.userSubject.asObservable();
  private http = inject(HttpClient);

  public authenticate(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth`, credentials, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  get utilisateur(): Utilisateur | null {
    return this.userSubject.value;
  }

  set utilisateur(value: Utilisateur | null) {
    console.log("User", value);
    this.userSubject.next(value);
  }

  public setTokenInStorage(token: string): void {
    sessionStorage.setItem('token', token);
  }

  public getTokenInStorage(): string | null {
    return sessionStorage.getItem('token');
  }

  public logout(): void{
    this.userSubject.next(null);
    sessionStorage.removeItem('token');
  }
}
