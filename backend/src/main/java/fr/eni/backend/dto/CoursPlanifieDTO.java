package fr.eni.backend.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursPlanifieDTO {
    private Integer id;
    private String titre;
    private Integer duree;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String idFormateur;  // ← String (car immatriculation est un String)
    private String nomFormateur;
    private Integer idPromotion;
    private Integer ordre;
}