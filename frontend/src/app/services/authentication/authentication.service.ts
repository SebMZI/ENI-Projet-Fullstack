import { inject, Injectable } from '@angular/core';
import { Authenticate } from '../../dto/authenticate';
import { HttpClient } from '@angular/common/http';
import {environment} from '../../../environments/environment';
import { Utilisateur } from '../../interfaces/utilisateur';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private userSubject = new BehaviorSubject<Utilisateur | null>(null);
  user$ = this.userSubject.asObservable();
  private http = inject(HttpClient);

  public authenticate(credentials: Authenticate): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(`${environment.apiUrl}/auth`, credentials, {
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

  public logout(): void{
    this.userSubject.next(null);
    // Call api suppression cookie
  }
}
