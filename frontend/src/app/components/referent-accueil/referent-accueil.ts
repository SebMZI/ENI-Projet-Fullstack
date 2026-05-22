import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

@Component({
  selector: 'app-referent-accueil',
  standalone: true,
  imports: [RouterModule, NavbarReferentComponent],
  templateUrl: './referent-accueil.html',
  styleUrl: './referent-accueil.css'
})
export class ReferentAccueilComponent {}