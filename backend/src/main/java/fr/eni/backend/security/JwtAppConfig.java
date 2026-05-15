package fr.eni.backend.security;

import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class JwtAppConfig {

    private final UtilisateurRepository utilisateurRepository;

    @Bean
    UserDetailsService userDetailsService(){
        return username -> {
            Utilisateur utilisateur = utilisateurRepository.findUtilisateurByEmailEni(username);
            if(utilisateur == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé");
            }
            return utilisateur;
        };
    }

    // Fournisseur d'authentification personnalisé
    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                UserDetails utilisateur = userDetailsService.loadUserByUsername(username);
                if (!passwordEncoder.matches(password, utilisateur.getPassword())) {
                  throw new BadCredentialsException("Bad credentials");
                }
                return new UsernamePasswordAuthenticationToken(utilisateur, password, utilisateur.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return false;
            }
        };
    }

    // Gestionnaire d'authentification basé sur le fournisseur personnalisé
    @Bean
    AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    // Encodeur de mdp
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
