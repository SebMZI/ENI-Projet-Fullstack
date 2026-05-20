package fr.eni.backend.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurResponseDTO {
    private String immatriculation;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private List<String> roles;
    private LocalDate dateCreation;
}
