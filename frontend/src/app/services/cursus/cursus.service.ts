import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { CursusDTO, CursusRequestDTO } from '../../interfaces/cursus';
import { CoursDTO } from '../../interfaces/cours'; 

@Injectable({ providedIn: 'root' })
export class CursusService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/cursus`;

  getAll(): Observable<CursusDTO[]> {
    return this.http.get<CursusDTO[]>(this.url);
  }

  getById(id: number): Observable<CursusDTO> {
    return this.http.get<CursusDTO>(`${this.url}/${id}`);
  }

  getCours(id: number): Observable<CoursDTO[]> {
    return this.http.get<CoursDTO[]>(`${environment.apiUrl}/cursus/${id}/cours`);
  }

  create(cursus: CursusRequestDTO): Observable<CursusDTO> {
    return this.http.post<CursusDTO>(this.url, cursus);
  }

  update(id: number, cursus: CursusRequestDTO): Observable<CursusDTO> {
    return this.http.put<CursusDTO>(`${this.url}/${id}`, cursus);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}