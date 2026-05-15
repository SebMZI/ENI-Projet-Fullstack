package fr.eni.projetformateurs.bo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class CoursPlanifie extends Cours {

    private LocalDate dateDebut;
    private LocalDate dateFin;
}
