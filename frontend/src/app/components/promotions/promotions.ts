import { Component, Inject } from '@angular/core';
import { Promotion } from '../../interfaces/promotion';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PromotionsService } from '../../services/promotions';
import { AuthenticationService } from '../../services/authentication/authentication.service';

@Component({
    selector: 'app-promotions',
    imports: [RouterLink],
    templateUrl: './promotions.html',
    styleUrl: './promotions.css',
})
export class Promotions {
    public promotions: Array<Promotion> = [];
    public immatriculation: string | null = '';

    constructor(
        private promotionService: PromotionsService,
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService,
        private router: Router,
    ) {}

    ngOnInit() {
        if (!this.isLogged()) {
            this.router.navigate(['/connexion']);
        }

        if (this.route.snapshot.paramMap.get('immatriculation')) {
            this.immatriculation = this.route.snapshot.paramMap.get('immatriculation');
            this.getFormateurPromotions();
        } else {
            this.getAllPromotions();
        }
    }

    public getFormateurPromotions(): void {
        this.promotionService
            .getFormateurPromotions(this.immatriculation)
            .subscribe((rep) => (this.promotions = rep));
    }

    public getAllPromotions(): void {
        this.promotionService.getAllPromotions().subscribe((rep) => (this.promotions = rep));
    }

    isLogged(): boolean {
        return !!this.authenticationService.getTokenInStorage();
    }
}
