package fr.eni.backend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "CURSUS")
public class Cursus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @NotBlank
    @Size(max = 250)
    @Column(name = "CURSUS_NAME", nullable = false, length = 250)
    private String intitule;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURSUS_ID")
    private @Builder.Default List<Cours> cours = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURSUS_ID")
    private @Builder.Default List<Promotion> promotions = new ArrayList<>();


}
