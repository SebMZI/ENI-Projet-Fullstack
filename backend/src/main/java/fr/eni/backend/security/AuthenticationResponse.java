package fr.eni.backend.security;

import fr.eni.backend.dto.UtilisateurDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthenticationResponse {
    private String token;
    private UtilisateurResponseDTO utilisateur = new UtilisateurResponseDTO();
}
