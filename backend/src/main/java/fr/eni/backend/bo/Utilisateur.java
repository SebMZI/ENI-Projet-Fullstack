package fr.eni.backend.bo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "DISCR")
//@DiscriminatorValue(value = "U")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "USER_REGISTRATION", nullable = false, unique = true)
    private String immatriculation;

    @Column(name = "USER_LAST_NAME", nullable = false, length = 100)
    private String nom;

    @Column(name = "USER_FIRST_NAME", nullable = false, length = 100)
    private String prenom;

    @Column(name = "USER_PASSWORD", nullable = false, length = 255)
    private String motDePasse;

    @Column(name = "USER_EMAIL", nullable = false, length = 150)
    private String email;

    @Column(name = "USER_EMAIL", nullable = false, length = 150)
    private String telephone;

    @Column(name = "CREATION_DATE")
    private LocalDate dateCreation;

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    private Adresse adresse;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_REGISTRATION")
    private @Builder.Default List<Role> roles = new ArrayList<>();
}
