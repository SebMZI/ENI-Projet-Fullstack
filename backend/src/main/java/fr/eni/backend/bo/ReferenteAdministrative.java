package fr.eni.backend.bo;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ReferenteAdministrative extends Utilisateur{

    @Column(name = "OFFICE", length = 100)
    private String bureau;
}
