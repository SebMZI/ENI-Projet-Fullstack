package fr.eni.backend.bo;

import fr.eni.backend.bo.key.RolePK;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50)
    @Column(name = "USER_REGISTRATION", nullable = false, length = 50)
    private String immatriculation;

    @Id
    @Column(name = "ROLE")
    private String role;
}
