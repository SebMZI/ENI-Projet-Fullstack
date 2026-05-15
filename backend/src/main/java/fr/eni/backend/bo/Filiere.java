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
@Table(name = "FIELD")
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "FIELD_NAME")
    private String nom;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIELD_ID")
    private @Builder.Default List<Cursus> cursus = new ArrayList<>();
}
