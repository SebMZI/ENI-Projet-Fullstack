import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { UtilisateursService } from '../../services/utilisateurs/utilisateurs.service';
import { Utilisateur } from '../../interfaces/utilisateur';
import { lastValueFrom, Observable } from 'rxjs';

@Component({
  selector: 'app-utilisateurs',
  imports: [RouterLink],
  templateUrl: './utilisateurs.html',
  styleUrl: './utilisateurs.css',
})
export class Utilisateurs {
  private uService = inject(UtilisateursService);
  public utilisateurs = signal<Utilisateur[]>([]);

  ngOnInit() {
    this.getAllUsers();
  }

  public async getAllUsers() {
    try {
      const response = await lastValueFrom(this.uService.getAllUsers());
      this.utilisateurs.set(response);
      console.log(response);
    } catch (e) {
      console.log(e);
    }
  }
}
