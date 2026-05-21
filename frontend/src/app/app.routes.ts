import { Routes } from '@angular/router';
import { Authentication } from './components/authentication/authentication';
import { Promotions } from './components/promotions/promotions';
import { promotionFormateurGuard } from './guards/promotion-formateur-guard';

export const routes: Routes = [
    { path: 'connexion', component: Authentication },
    { path: 'promotions/formateurs/:immatriculation', component: Promotions, canActivate: [promotionFormateurGuard] },
];
