import { Component, inject, signal } from '@angular/core';
import { Utilisateur } from '../../interfaces/utilisateur';
import { ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { UtilisateursService } from '../../services/utilisateurs/utilisateurs.service';
import { Utilisateurs } from '../utilisateurs/utilisateurs';
import { UtilisateurForm } from '../utilisateur-form/utilisateur-form';

@Component({
  selector: 'app-modifier-utilisateur',
  imports: [UtilisateurForm],
  templateUrl: './modifier-utilisateur.html',
  styleUrl: './modifier-utilisateur.css',
})
export class ModifierUtilisateur {
  public errorMsg: string | undefined = undefined;
  public userImmat = signal('');
  private route = inject(ActivatedRoute);
  private uService: UtilisateursService = inject(UtilisateursService);

  public utilisateur = signal<Utilisateur>({
    email: '',
    immatriculation: '',
    nom: '',
    prenom: '',
    roles: [],
  });

  constructor() {
    this.route.params.subscribe((params) => {
      this.userImmat.set(params['id']);
      this.getOneUser(this.userImmat());
    });
  }

  ngOnInit() {}

  public async getOneUser(userImmat: string) {
    try {
      const response = await lastValueFrom(this.uService.findOneByImmat(userImmat));
      console.log('user', response);
      this.utilisateur.set(response);
    } catch (error) {
      console.log(error);
    }
  }

  public onNewUser(user: Utilisateur) {
    console.log('New User: ', user);
  }
}
