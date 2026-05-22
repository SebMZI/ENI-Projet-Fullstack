package fr.eni.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurDTO {
    private String immatriculation;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String[] roles;
    private LocalDate dateCreation;

    // Spécifiques (null si non concerné)
    private String bureau;          // Referente
    private String statut;          // Formateur
    private String service;         // Administrateur
    private String emailPersonnel;  // Eleve
    private LocalDate dateInscription; // Eleve
}