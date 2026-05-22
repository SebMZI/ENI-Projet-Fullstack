package fr.eni.backend.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionDTO {
    private Integer id;
    private LocalDate dateInscription;
    private String type;
    private boolean forcee;
    private String eleveImmatriculation;
    private Integer coursPlanifieId;
    private String titreCours;
}