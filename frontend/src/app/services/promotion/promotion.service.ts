import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { PromotionDTO, PromotionRequestDTO } from '../../interfaces/promotion';

@Injectable({ providedIn: 'root' })
export class PromotionService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/promotions`;

  getAll(): Observable<PromotionDTO[]> {
    return this.http.get<PromotionDTO[]>(this.url);
  }

  getById(id: number): Observable<PromotionDTO> {
    return this.http.get<PromotionDTO>(`${this.url}/${id}`);
  }
  
  getByEleve(immatriculation: string): Observable<PromotionDTO[]> {
    return this.http.get<PromotionDTO[]>(`${environment.apiUrl}/promotions/eleves/${immatriculation}/promotions`);
  }
  create(promotion: PromotionRequestDTO): Observable<PromotionDTO> {
    return this.http.post<PromotionDTO>(this.url, promotion);
  }

  update(id: number, promotion: PromotionRequestDTO): Observable<PromotionDTO> {
    return this.http.put<PromotionDTO>(`${this.url}/${id}`, promotion);
  }

  inscrireEleve(promotionId: number, eleveId: string): Observable<PromotionDTO> {
    return this.http.post<PromotionDTO>(`${this.url}/${promotionId}/eleves/${eleveId}`, {});
  }
  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}