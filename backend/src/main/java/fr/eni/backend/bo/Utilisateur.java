package fr.eni.projetformateurs.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "USER")
public class Utilisateur {

    @Id
    @Column(name = "USER_REGISTRATION", nullable = false, unique = true)
    private String immatriculation;

    @Column(name = "USER_LAST_NAME", nullable = false, length = 100)
    private String nom;

    @Column(name = "USER_FIRST_NAME", nullable = false, length = 100)
    private String prenom;

    @Column(name = "USER_PASSWORD", nullable = false, length = 255)
    private String motDePasse;

    @Column(name = "USER_EMAIL", nullable = false, length = 150)
    private String emailEni;

    @Column(name = "USER_EMAIL", nullable = false, length = 150)
    private String telephone;

    @Column(name = "CREATION_DATE")
    private LocalDate dateCreation;
}
