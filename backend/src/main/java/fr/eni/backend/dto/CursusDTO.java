package fr.eni.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursusDTO {
    private Integer id;
    private String intitule;
    private Integer idFiliere;
}