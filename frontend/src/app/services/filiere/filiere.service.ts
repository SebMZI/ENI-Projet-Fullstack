import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { FiliereDTO, FiliereRequestDTO } from '../../interfaces/filiere';

@Injectable({ providedIn: 'root' })
export class FiliereService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/filieres`;

  getAll(): Observable<FiliereDTO[]> {
    return this.http.get<FiliereDTO[]>(this.url);
  }

  getById(id: number): Observable<FiliereDTO> {
    return this.http.get<FiliereDTO>(`${this.url}/${id}`);
  }

  create(filiere: FiliereRequestDTO): Observable<FiliereDTO> {
    return this.http.post<FiliereDTO>(this.url, filiere);
  }

  update(id: number, filiere: FiliereRequestDTO): Observable<FiliereDTO> {
    return this.http.put<FiliereDTO>(`${this.url}/${id}`, filiere);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}