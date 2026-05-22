import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { CoursPlanifieDTO, CoursPlanifieRequestDTO } from '../../interfaces/cours-planifie';

@Injectable({ providedIn: 'root' })
export class CoursPlanifieService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/cours-planifies`;


  getAll(): Observable<CoursPlanifieDTO[]> {
    return this.http.get<CoursPlanifieDTO[]>(this.url);
  }
  getByPromotion(promotionId: number): Observable<CoursPlanifieDTO[]> {
    return this.http.get<CoursPlanifieDTO[]>(`${this.url}/promotion/${promotionId}`);
  }

  create(promotionId: number, request: CoursPlanifieRequestDTO): Observable<CoursPlanifieDTO> {
    return this.http.post<CoursPlanifieDTO>(`${this.url}/promotion/${promotionId}`, request);
  }

  update(id: number, request: CoursPlanifieRequestDTO): Observable<CoursPlanifieDTO> {
    return this.http.put<CoursPlanifieDTO>(`${this.url}/${id}`, request);
  }
  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}