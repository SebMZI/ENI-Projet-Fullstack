import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

export interface UtilisateurDTO {
  immatriculation: string;
  nom: string;
  prenom: string;
  email: string;
  roles: string[];
  telephone?: string;
  statut?: string;
  bureau?: string;
  service?: string;
  emailPersonnel?: string;
  dateInscription?: string;
  dateCreation?: string;
}

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/utilisateurs`;

  getAll(): Observable<UtilisateurDTO[]> {
    return this.http.get<UtilisateurDTO[]>(this.url);
  }
}