package fr.eni.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EleveDTO {
    private String immatriculation;
    private String nom;
    private String prenom;
    private String email;
}