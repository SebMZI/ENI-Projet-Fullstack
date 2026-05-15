package fr.eni.backend.bo;

import jakarta.persistence.*;
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

    @Column(name = "COURSE_TITLE", nullable = false, length = 250)
    private String titre;

    @Column(name = "COURSE_DURATION", nullable = false)
    private Integer duree;
}
