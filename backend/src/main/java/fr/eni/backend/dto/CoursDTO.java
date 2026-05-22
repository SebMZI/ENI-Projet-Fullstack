package fr.eni.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursDTO {
    private Integer id;
    private String titre;
    private Integer duree;
    private Integer ordre;
    private Integer idCursus;
}