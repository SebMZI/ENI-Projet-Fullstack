import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Utilisateur } from '../../interfaces/utilisateur';
import { AuthenticationService } from '../authentication/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class UtilisateursService {
  private auth: AuthenticationService = inject(AuthenticationService);
  private http: HttpClient = inject(HttpClient);

  public getAllUsers(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${environment.apiUrl}/utilisateurs`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${this.auth.getTokenInStorage()}`,
      },
    });
  }

  public findOneByImmat(userImmat: string): Observable<Utilisateur> {
    return this.http.get<Utilisateur>(`${environment.apiUrl}/utilisateurs/${userImmat}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${this.auth.getTokenInStorage()}`,
      },
    });
  }

  public editUser(user: Utilisateur) {
    return this.http.put(`${environment.apiUrl}/${user.immatriculation}`, user, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${this.auth.getTokenInStorage()}`,
      },
    });
  }

  public addUser(user: Utilisateur): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(`${environment.apiUrl}/utilisateurs`, user, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${this.auth.getTokenInStorage()}`,
      },
    });
  }

  public generateTemporaryPassword(): string {
    let pwd: string = "";
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const maxChar = 10;
    for (let i = 0; i < maxChar; i++) {
      pwd += characters[Math.floor(Math.random() * maxChar)];
    }

    return pwd;
  }

  public generateImmatriculation():string {
    let immat: string = "";
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const maxChar = 4;

    const now = new Date();
    const year = now.getFullYear().toString().slice(2, 4);
    const month = now.getMonth() + 1;

    immat = "ENI"
    immat = immat + year + month
    for(let i = 0; i < maxChar; i++) {
      immat += characters[Math.floor(Math.random() * maxChar)];
    }

    return immat;
  }
}
