import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PromotionsService } from './promotions';

@Injectable({
    providedIn: 'root',
})
export class PromotionDetailsService {
    public BASE_URL: string = 'http://localhost:8080/api/utilisateurs/';

    constructor(
        private http: HttpClient,
        private promotionService: PromotionsService,
    ) {}

    public getPromotionEleves(id: string | null): Observable<any> {
        return this.http.get<any>(this.BASE_URL + 'promotions/' + id + '/eleves');
    }
}
