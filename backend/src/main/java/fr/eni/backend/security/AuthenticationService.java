package fr.eni.backend.security;

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

        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmailEni(request.getPseudo());

        String jwtToken = jwtService.generateToken(utilisateur);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        return authenticationResponse;
    }
}
