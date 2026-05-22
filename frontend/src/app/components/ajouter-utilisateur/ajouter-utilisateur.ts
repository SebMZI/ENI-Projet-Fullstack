import { Component, inject, signal } from '@angular/core';
import { UtilisateurForm } from '../utilisateur-form/utilisateur-form';
import { Utilisateur } from '../../interfaces/utilisateur';
import { UtilisateursService } from '../../services/utilisateurs/utilisateurs.service';
import { lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-ajouter-utilisateur',
  imports: [UtilisateurForm],
  templateUrl: './ajouter-utilisateur.html',
  styleUrl: './ajouter-utilisateur.css',
})
export class AjouterUtilisateur {
  public createdUser = signal<Utilisateur>({
    adresse: undefined,
    bureau: undefined,
    dateCreation: undefined,
    dateInscription: undefined,
    email: "",
    emailPersonnel: undefined,
    immatriculation: "",
    motDePasse: undefined,
    nom: "",
    prenom: "",
    roles: [],
    service: undefined,
    statut: undefined,
    telephone: undefined
  });

  private utilisateurService: UtilisateursService = inject(UtilisateursService);
  public errorMsg: string | undefined = undefined;

  public async onNewUser(user: Utilisateur) {
    console.log("New User: ", user);
    const immatriculation: string = this.utilisateurService.generateImmatriculation();
    const tempPwd: string=  this.utilisateurService.generateTemporaryPassword();
    user.immatriculation = immatriculation;
    user.motDePasse = tempPwd;

    try {
      const response = await lastValueFrom(this.utilisateurService.addUser(user));
      response.motDePasse = user.motDePasse;
      this.createdUser.set(response);
      console.log("Resposne", response);
    }catch(error) {
      console.log("Error", error);
    }
    // TODO retourner tempPWd et immat au form
  };
}
