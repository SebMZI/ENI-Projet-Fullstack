package fr.eni.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;
@Data
public class UtilisateurRequestDTO {

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank @Email
    @Pattern(regexp = "^[\\w-\\.]+@campus-eni.fr$",
             message = "Email doit être au format *@campus-eni.fr")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
    
    @Size(max = 10)
    private String telephone;

    @NotEmpty(message = "Au moins un rôle est requis")
    private List<String> roles; // ["ELEVE", "FORMATEUR"]

    // Champs spécifiques selon le rôle — optionnels
    private String statut;    // Formateur
    private String bureau;    // Referente
    private String service;   // Administrateur
    private String emailPersonnel; // Eleve
}