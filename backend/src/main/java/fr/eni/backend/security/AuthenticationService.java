package fr.eni.backend.security;

import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private UtilisateurRepository utilisateurRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPseudo(), request.getPassword()));

        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmail(request.getPseudo());

        UtilisateurResponseDTO utilisateurDto = UtilisateurResponseDTO.builder()
                .immatriculation(utilisateur.getImmatriculation())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .nom(utilisateur.getNom())
                .roles(utilisateur.getRoles().stream().map(Role::getRole).toList()).telephone(utilisateur.getTelephone()).dateCreation(utilisateur.getDateCreation()).build();

        String jwtToken = jwtService.generateToken(utilisateur);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setUtilisateur(utilisateurDto);

        return authenticationResponse;
    }
}
