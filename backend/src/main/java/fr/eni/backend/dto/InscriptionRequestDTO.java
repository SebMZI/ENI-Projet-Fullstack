package fr.eni.backend.dto;

import lombok.Data;

@Data
public class InscriptionRequestDTO {
    private String immatriculation;
    private Integer coursPlanifieId;
}