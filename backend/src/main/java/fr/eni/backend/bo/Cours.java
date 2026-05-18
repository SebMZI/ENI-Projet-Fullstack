package fr.eni.backend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "COURSE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    @Size(max=255)
    @Column(name = "COURSE_TITLE", nullable = false, length = 250)
    private String titre;

    @NotNull
    @NotBlank
    @Column(name = "COURSE_DURATION", nullable = false)
    private Integer duree;
}
