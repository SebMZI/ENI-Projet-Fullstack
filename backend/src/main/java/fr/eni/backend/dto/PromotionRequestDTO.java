package fr.eni.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PromotionRequestDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "L'id du cursus est obligatoire")
    private Integer idCursus;
}