export interface CoursPlanifieDTO {
  id: number;
  titre: string;
  duree: number;
  dateDebut: string;
  dateFin: string;
  idFormateur: string | null;
  nomFormateur: string | null;
  idPromotion: number;
  ordre: number | null;
}

export interface CoursPlanifieRequestDTO {
  idCours: number;
  dateDebut: string;
  dateFin: string;
  idFormateur?: string;
}