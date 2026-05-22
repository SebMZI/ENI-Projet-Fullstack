import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { CoursDTO, CoursRequestDTO } from '../../interfaces/cours';

@Injectable({ providedIn: 'root' })
export class CoursService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/cours`;

  getAll(): Observable<CoursDTO[]> {
    return this.http.get<CoursDTO[]>(this.url);
  }

  getById(id: number): Observable<CoursDTO> {
    return this.http.get<CoursDTO>(`${this.url}/${id}`);
  }

  create(cours: CoursRequestDTO): Observable<CoursDTO> {
    return this.http.post<CoursDTO>(this.url, cours);
  }

  update(id: number, cours: CoursRequestDTO): Observable<CoursDTO> {
    return this.http.put<CoursDTO>(`${this.url}/${id}`, cours);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}