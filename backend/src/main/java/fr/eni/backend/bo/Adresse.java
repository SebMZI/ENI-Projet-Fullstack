package fr.eni.backend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @NotBlank
    @Size(max=255)
    @Column(name = "STREET", nullable = false, length = 255)
    private String rue;

    @NotNull
    @NotBlank
    @Size(max=10)
    @Column(name = "POSTAL_CODE", nullable = false, length = 10)
    private String codePostal;

    @NotNull
    @NotBlank
    @Size(max=100)
    @Column(name = "CITY", nullable = false, length = 100)
    private String ville;
}
