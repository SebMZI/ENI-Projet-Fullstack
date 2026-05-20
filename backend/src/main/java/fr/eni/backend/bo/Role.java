package fr.eni.backend.bo;

import fr.eni.backend.bo.key.RolePK;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "ROLE")
@IdClass(RolePK.class)
public class Role {
    @Id
    @NonNull
    @NotBlank
    @Size(max = 50)
    @Column(name = "USER_REGISTRATION", nullable = false, length = 50)
    private String immatriculation;

    @Id
    @Column(name = "ROLE")
    private String role;

    // Relation ManyToOne vers Utilisateur
    @ManyToOne
    @JoinColumn(name = "USER_REGISTRATION", insertable = false, updatable = false)
    private Utilisateur utilisateur;
}
