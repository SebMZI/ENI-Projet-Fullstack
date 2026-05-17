package fr.eni.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoursRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotNull(message = "La durée est obligatoire")
    private Integer duree;
}