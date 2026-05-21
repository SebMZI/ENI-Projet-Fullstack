import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthenticationService } from '../services/authentication/authentication.service';

export const promotionFormateurGuard: CanActivateFn = (route, state) => {

    const authService = inject(AuthenticationService)
    const router = inject(Router);

    const immatriculation = route.paramMap.get('immatriculation');

    if (authService.utilisateur?.immatriculation !== immatriculation)
        router.navigate(['/promotions/formateurs/:immatriculation']);
    return true;
};
