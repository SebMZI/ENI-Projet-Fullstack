import { Routes } from '@angular/router';
import { Filiere } from './components/filieres/filiere';
import { Authentication } from './components/authentication/authentication';
import { Cursus } from './components/cursus/cursus';
import { Cours } from './components/cours/cours';
import { Promotions } from './components/promotion/promotion';
import { CoursPlanifie } from './components/cours-planifie/cours-planifie';
import { Eleves } from './components/eleves/eleves';
import { ReferentAccueilComponent } from './components/referent-accueil/referent-accueil';
import { Utilisateurs } from './components/utilisateurs/utilisateurs';
import { ModifierUtilisateur } from './components/modifier-utilisateur/modifier-utilisateur';
import { AjouterUtilisateur } from './components/ajouter-utilisateur/ajouter-utilisateur';

export const routes: Routes = [
  { path: 'connexion', component: Authentication },
  { path: 'utilisateurs', component: Utilisateurs },
  { path: 'utilisateurs/ajouter', component: AjouterUtilisateur },
  { path: 'utilisateurs/:id', component: ModifierUtilisateur },
  { path: 'filieres', component: Filiere },
  { path: 'cursus', component: Cursus },
  { path: 'cours', component: Cours },
  { path: 'promotions', component: Promotions },
  { path: 'promotions/:id/cours-planifies', component: CoursPlanifie },
  { path: 'eleves', component: Eleves },
  { path: 'referent', component: ReferentAccueilComponent },
  { path: '', redirectTo: '/filieres', pathMatch: 'full' }
];