package fr.eni.backend.bo;

import jakarta.persistence.*;
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
@Table(name = "SCHEDULED_COURSE")
public class CoursPlanifie extends Cours {

    @Column(name = "START_DATE", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate dateFin;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TRAINER_ID")
    private Formateur formateur;
}
