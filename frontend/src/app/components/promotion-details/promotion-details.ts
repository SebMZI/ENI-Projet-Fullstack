import { Component } from '@angular/core';
import { PromotionDetailsService } from '../../services/promotion-details';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Eleve } from '../../interfaces/eleve';

@Component({
    selector: 'app-promotion-details',
    imports: [],
    templateUrl: './promotion-details.html',
    styleUrl: './promotion-details.css',
})
export class PromotionDetails {
    public eleves: Array<Eleve> = [];
    //public cours: Array<Cours> = [];
    public idPromotion: string | null = '';

    constructor(
        private promotionDetailsService: PromotionDetailsService,
        private route: ActivatedRoute,
    ) {}

    ngOnInit() {
        if (this.route.snapshot.paramMap.get('idPromotion')) {
            this.idPromotion = this.route.snapshot.paramMap.get('idPromotion');
        }

        this.getPromotionEleves(this.idPromotion);
    }

    public getPromotionEleves(promotionId: string | null) {
        this.promotionDetailsService
            .getPromotionEleves(promotionId)
            .subscribe((rep) => (this.eleves = rep));
    }
}
