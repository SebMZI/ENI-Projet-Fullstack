package fr.eni.backend.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionDTO {
    private Integer id;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private Integer idCursus;   
}