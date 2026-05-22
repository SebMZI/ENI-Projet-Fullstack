import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

export interface InscriptionRequestDTO {
  immatriculation: string;
  coursPlanifieId: number;
}

export interface InscriptionDTO {
  id: number;
  dateInscription: string;
  type: string;
  forcee: boolean;
  eleveImmatriculation: string;
  coursPlanifieId: number;
  titreCours: string;
}

@Injectable({ providedIn: 'root' })
export class InscriptionService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/inscriptions`;

  inscrireUnite(request: InscriptionRequestDTO): Observable<InscriptionDTO> {
    return this.http.post<InscriptionDTO>(`${this.url}/unite`, request);
  }

  inscrireUniteForcee(request: InscriptionRequestDTO): Observable<InscriptionDTO> {
    return this.http.post<InscriptionDTO>(`${this.url}/unite/forcer`, request);
  }

  getByEleve(immatriculation: string): Observable<InscriptionDTO[]> {
    return this.http.get<InscriptionDTO[]>(`${this.url}/eleve/${immatriculation}`);
    }
}