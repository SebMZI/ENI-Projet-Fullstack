package fr.eni.backend.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adresse {

    private Integer id;
    private String rue;
    private String codePostal;
    private String ville;
}
