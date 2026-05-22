export interface CoursDTO {
  id: number;
  titre: string;
  duree: number;
  idCursus: number | null;
  ordre: number | null;
}

export interface CoursRequestDTO {
  titre: string;
  duree: number;
  idCursus: number;
  ordre: number;
}