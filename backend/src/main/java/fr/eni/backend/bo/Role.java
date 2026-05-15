package fr.eni.backend.bo;

import fr.eni.backend.bo.key.RolePK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "ROLE")
@IdClass(RolePK.class)
public class Role {

    @Id
    @Column(name = "USER_REGISTRATION", nullable = false)
    private String immatriculation;

    @Id
    @Column(name = "ROLE")
    private String role;
}
