package fr.eni.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CoursPlanifieRequestDTO {
    @NotNull(message = "L'ID du cours est obligatoire")
    private Integer idCours;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    private String  idFormateur;  // Optionnel
}