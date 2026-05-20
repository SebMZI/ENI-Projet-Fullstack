import { Utilisateur } from './utilisateur';

export interface AuthResponse {
  token: string;
  utilisateur: Utilisateur;
}
