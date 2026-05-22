export interface PromotionDTO {
  id: number;
  nom: string;
  dateDebut: string;
  dateFin: string;
  idCursus: number | null;
}

export interface PromotionRequestDTO {
  nom: string;
  dateDebut: string;
  dateFin: string;
  idCursus: number;
}