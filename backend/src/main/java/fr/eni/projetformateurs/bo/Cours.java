package fr.eni.projetformateurs.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cours {

    private Integer id;
    private String reference;
    private String intitule;
    private Integer duree;
}
