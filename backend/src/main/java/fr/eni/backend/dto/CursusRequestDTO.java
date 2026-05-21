package fr.eni.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CursusRequestDTO {

    @NotBlank(message = "L'intitulé est obligatoire")
    private String intitule;

    @NotNull(message = "La filière est obligatoire")
    private Integer idFiliere;
}