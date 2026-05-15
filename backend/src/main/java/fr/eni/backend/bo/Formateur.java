package fr.eni.projetformateurs.bo;

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
public class Formateur extends Utilisateur{

    @Column(name = "TEACHER_STATUS", length = 100)
    private String statut;
}
