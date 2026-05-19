package fr.eni.backend.security;

import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.UtilisateurRepository;
import fr.eni.backend.dto.UtilisateurDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@AllArgsConstructor
@Service
public class AuthenticationService {
    private UtilisateurRepository utilisateurRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPseudo(), request.getPassword()));

        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmail(request.getPseudo());

        UtilisateurDto utilisateurDto = UtilisateurDto.builder()
                .immatriculation(utilisateur.getImmatriculation())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .nom(utilisateur.getNom())
                .roles(utilisateur.getRoles().stream().map(Role::getRole).toList()).build();
        String jwtToken = jwtService.generateToken(utilisateur);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwtToken);
        authenticationResponse.setUtilisateurDto(utilisateurDto);

        return authenticationResponse;
    }

    public UtilisateurDto checkLoggedIn(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BadCredentialsException("Invalid token: cookie missing");
        }

        Cookie jwtCookie = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("jwt_token_app_erp")).findFirst().orElseThrow(() -> new BadCredentialsException("Invalid token: cookie missing"));

        String jwt = jwtCookie.getValue();
        if(jwt == null) {
            throw new BadCredentialsException("Invalid token: empty value");
        }

        final String email = jwtService.extractUserName(jwt);
        if (email == null) {
            throw new BadCredentialsException("Bad credentials: email not found in token");
        }

        Utilisateur utilisateur = (Utilisateur) userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(jwt, utilisateur)) {
            throw new BadCredentialsException("Invalid token: token not valid");
        }


        return UtilisateurDto.builder()
                .immatriculation(utilisateur.getImmatriculation())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .nom(utilisateur.getNom())
                .roles(utilisateur.getRoles().stream().map(Role::getRole).toList()).build();
    }
}
