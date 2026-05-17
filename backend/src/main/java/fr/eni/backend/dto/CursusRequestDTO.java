package fr.eni.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CursusRequestDTO {

    @NotBlank(message = "L'intitulé est obligatoire")
    private String intitule;
}