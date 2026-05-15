package fr.eni.backend.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "ADDRESS")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "STREET", nullable = false, length = 255)
    private String rue;

    @Column(name = "POSTAL_CODE", nullable = false, length = 10)
    private String codePostal;


    @Column(name = "CITY", nullable = false, length = 100)
    private String ville;
}
