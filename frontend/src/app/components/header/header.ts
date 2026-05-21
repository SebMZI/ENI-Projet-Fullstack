import { Component } from '@angular/core';
import { AuthenticationService } from '../../services/authentication/authentication.service';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-header',
    imports: [RouterLink],
    templateUrl: './header.html',
    styleUrl: './header.css',
})
export class Header {
    public userName: string = '';
    public userRegistration: string = '';
    public userRoles: string[] = [];

    constructor(private authentication: AuthenticationService) {}

    ngOnInit() {
        this.userName = this.authentication.utilisateur
            ? this.authentication.utilisateur.email
            : '';
        this.userRoles = this.authentication.utilisateur
            ? this.authentication.utilisateur.roles
            : [];
        this.userRegistration = this.authentication.utilisateur
            ? this.authentication.utilisateur.immatriculation
            : '';
    }

    isLogged(): boolean {
        return !!this.authentication.getTokenInStorage();
    }

    logout(): void {
        this.authentication.logout();
    }
}
