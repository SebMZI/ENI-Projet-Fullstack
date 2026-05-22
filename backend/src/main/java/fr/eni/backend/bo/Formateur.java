package fr.eni.backend.bo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder

@Entity
@Table(name = "TRAINER")
//@DiscriminatorValue(value = "F")
public class Formateur extends Utilisateur{

    @Size(max = 100)
    @Column(name = "TEACHER_STATUS", length = 100)
    private String statut;
}
