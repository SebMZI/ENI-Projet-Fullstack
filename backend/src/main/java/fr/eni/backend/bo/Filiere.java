package fr.eni.backend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NonNull
    @NotBlank
    @Size(max = 100)
    @Column(name = "FIELD_NAME")
    private String nom;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIELD_ID")
    private @Builder.Default List<Cursus> cursus = new ArrayList<>();
}
