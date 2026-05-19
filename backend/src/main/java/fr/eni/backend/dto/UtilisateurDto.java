package fr.eni.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurDto {
    private String immatriculation;
    private String email;
    private String prenom;
    private String nom;
    private List<String> roles;
}
