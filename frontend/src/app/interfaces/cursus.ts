export interface CursusDTO {
  id: number;
  intitule: string;
  idFiliere: number | null;
}

export interface CursusRequestDTO {
  intitule: string;
  idFiliere: number;
}