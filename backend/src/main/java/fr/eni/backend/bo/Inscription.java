package fr.eni.backend.bo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "INSCRIPTION")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "INSCRIPTION_DATE")
    private LocalDate dateInscription;

    @Column(name = "INSCRIPTION_TYPE", nullable = false)
    private String type; // UNITE ou PROMOTION

    @Column(name = "IS_FORCED")
    private boolean forcee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private Eleve eleve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULED_COURSE_ID")
    private CoursPlanifie coursPlanifie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID")
    private Promotion promotion;
}