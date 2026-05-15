package fr.eni.backend.bo;

import jakarta.persistence.*;
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

    @Column(name = "CURSUS_NAME", nullable = false, length = 250)
    private String intitule;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CURSUS_ID")
    private @Builder.Default List<Cours> cours = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURSUS_ID")
    private @Builder.Default List<Promotion> promotions = new ArrayList<>();


}
