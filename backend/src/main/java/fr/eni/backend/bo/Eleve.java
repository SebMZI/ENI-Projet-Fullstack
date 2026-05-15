package fr.eni.backend.bo;

import jakarta.persistence.Column;
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
public class Eleve extends Utilisateur{

    @Column(name = "PERSONNAL_EMAIL", length = 150)
    private String emailPersonnel;

    @Column(name = "TEACHER_STATUS", length = 100)
    private LocalDate dateInscription;

    @Column(name = "TEACHER_PHONE", length = 100)
    private String telephone;
}
