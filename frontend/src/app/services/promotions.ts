import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class PromotionsService {
    public BASE_URL: string = 'http://localhost:8080/api/promotions/';

    constructor(private http: HttpClient) {}

    public getFormateurPromotions(immatriculation: string | null): Observable<any> {
        return this.http.get<any>(this.BASE_URL + immatriculation);
    }

    public getAllPromotions(): Observable<any> {
        return this.http.get<any>(this.BASE_URL);
    }
}
