package fr.eni.backend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "PROMOTION")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @NotBlank
    @Size(max = 100)
    @Column(name="PROMOTION_NAME", length = 100)
    private String nom;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate dateFin;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PROMOTION_ID")
    private @Builder.Default List<CoursPlanifie> coursPlanifies = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "PROMOTION_STUDENT",
    joinColumns = {@JoinColumn(name = "PROMOTION_ID")},
    inverseJoinColumns = {@JoinColumn(name = "STUDENT_ID")})
    @ToString.Exclude
    private @Builder.Default List<Eleve> eleves = new ArrayList<>();
}
