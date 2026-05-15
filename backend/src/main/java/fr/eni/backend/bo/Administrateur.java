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
public class Administrateur extends Utilisateur{

    @Column(name = "ADMINISTRATOR_SERVICE", length = 100)
    private String service;
}
