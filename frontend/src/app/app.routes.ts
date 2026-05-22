import { Routes } from '@angular/router';
import { Authentication } from './components/authentication/authentication';
import { Promotions } from './components/promotions/promotions';
import { promotionFormateurGuard } from './guards/promotion-formateur-guard';
import { PromotionDetails } from './components/promotion-details/promotion-details';

export const routes: Routes = [
    { path: 'connexion', component: Authentication },
    { path: 'promotions/formateurs/:immatriculation', component: Promotions, canActivate: [promotionFormateurGuard] },
    { path: 'promotions/:idPromotion/details', component: PromotionDetails }
];
