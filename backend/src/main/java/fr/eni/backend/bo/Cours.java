package fr.eni.backend.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Cours {

    private Integer id;
    private String reference;
    private String intitule;
    private Integer duree;
}
