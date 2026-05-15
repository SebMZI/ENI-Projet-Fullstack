package fr.eni.projetformateurs.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {

    private String numero;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
