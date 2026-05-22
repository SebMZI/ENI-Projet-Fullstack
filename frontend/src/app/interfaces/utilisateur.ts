import { Adresse } from './adresse';

export interface Utilisateur {
  immatriculation: string;
  email: string;
  prenom: string;
  nom: string;
  roles: string[];
  telephone?: string;
  dateCreation?: Date;
  bureau?: string;
  statut?: string;
  service?: string;
  emailPersonnel?: string;
  dateInscription?: string;
  adresse?: Adresse;
  motDePasse?: string;
}
