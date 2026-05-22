import { Routes } from '@angular/router';
import { Authentication } from './components/authentication/authentication';
import { Utilisateurs } from './components/utilisateurs/utilisateurs';
import { ModifierUtilisateur } from './components/modifier-utilisateur/modifier-utilisateur';
import { AjouterUtilisateur } from './components/ajouter-utilisateur/ajouter-utilisateur';

export const routes: Routes = [
  { path: 'connexion', component: Authentication },
  { path: 'utilisateurs', component: Utilisateurs },
  { path: 'utilisateurs/ajouter', component: AjouterUtilisateur },
  { path: 'utilisateurs/:id', component: ModifierUtilisateur },
];
