package fr.eni.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.RoleRepository;
import fr.eni.backend.dao.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TestAuth {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationService aService;
    @Autowired
    private PasswordEncoder pEncoder;

    Utilisateur annelise;

    @BeforeEach
    void createUser() {
        utilisateurRepository.deleteAll();
        roleRepository.deleteAll();


        Role roleReferent = Role.builder()
                .role("ROLE_REFERENTE")
                .build();

        roleRepository.saveAndFlush(roleReferent);

        String mdpEncoded = pEncoder.encode("JeSuisAnneLise");

        annelise = Utilisateur.builder()
                .immatriculation("ENI_25039285")
                .nom("BAILLE")
                .prenom("Anne-Lise")
                .motDePasse(mdpEncoded)
                .email("abaille@campus-eni.fr")
                .telephone("0600000000")
                .build();

        annelise.getRoles().add(roleReferent);

        utilisateurRepository.saveAndFlush(annelise);

        Utilisateur saved = utilisateurRepository.findUtilisateurByEmail("abaille@campus-eni.fr");
        assertThat(saved).isNotNull();
    }

    @Test
    void test_auth_correct_credentials() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        AuthenticationResponse response = aService.authenticate(request);
        assertThat(response.getUtilisateur()).isNotNull();
    }

    @Test
    void test_auth_incorrect_credentials(){
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("Jefwehfuwhefui");

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> aService.authenticate(request));
        assertThat(exception.getMessage()).isEqualTo("Bad credentials");
    }

    @Test
    void test_auth_incorrect_username() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("gjihwwihfwe@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> aService.authenticate(request));
        assertThat(exception.getMessage()).isEqualTo("Utilisateur non trouvé");
    }

    @Test
    void test_auth_route_permit_all() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_auth_dto() throws Exception{
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");


        mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.utilisateur.email").value("abaille@campus-eni.fr"))
                .andExpect(jsonPath("$.utilisateur.prenom").value("Anne-Lise"))
                .andExpect(jsonPath("$.utilisateur.nom").value("BAILLE"))
                .andExpect(jsonPath("$.utilisateur.roles").isArray())
                .andExpect(jsonPath("$.utilisateur.roles[0]").value("ROLE_REFERENT"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void test_auth_dto_no_pwd() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");


        mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.utilisateur.password").doesNotExist());
    }
}
