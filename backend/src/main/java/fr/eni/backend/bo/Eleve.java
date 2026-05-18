package fr.eni.backend.bo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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

@Entity
@Table(name = "STUDENT")
//@DiscriminatorValue(value = "E")
public class Eleve extends Utilisateur {

    @Email
    @Column(name = "PERSONNAL_EMAIL", length = 150)
    private String emailPersonnel;

    @Column(name = "STUDENT_SUBSCRIPTION_DATE", length = 100)
    private LocalDate dateInscription;

}
