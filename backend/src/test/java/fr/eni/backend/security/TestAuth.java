package fr.eni.backend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.RoleRepository;
import fr.eni.backend.dao.UtilisateurRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void createUser() {
        roleRepository.deleteAll();
        utilisateurRepository.deleteAll();

        String mdpEncoded = pEncoder.encode("JeSuisAnneLise");

        Utilisateur utilisateur = Utilisateur.builder()
                .immatriculation("ENI_25039285")
                .nom("BAILLE")
                .prenom("Anne-Lise")
                .motDePasse(mdpEncoded)
                .email("abaille@campus-eni.fr")
                .telephone("0600000000")
                .build();

        utilisateurRepository.saveAndFlush(utilisateur);

        Role roleAdmin = Role.builder()
                .immatriculation("ENI_25039285")
                .role("ROLE_ADMIN")
                .build();

        roleRepository.saveAndFlush(roleAdmin);

        Utilisateur saved = utilisateurRepository.findUtilisateurByEmail("abaille@campus-eni.fr");
        assertThat(saved).isNotNull();
    }

    @Test
    void test_auth_correct_credentials() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        AuthenticationResponse response = aService.authenticate(request);
        assertThat(response.getToken()).isNotNull();
    }

    @Test
    void test_auth_incorrect_credentials(){
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("Jefwehfuwhefui");
        boolean thrown = false;
        String errorMessage = "";

        try {
            AuthenticationResponse response = aService.authenticate(request);
        }catch (BadCredentialsException e) {
            thrown = true;
            errorMessage = e.getMessage();
        }

        assertThat(thrown).isTrue();
        assertThat(errorMessage).isEqualTo("Bad credentials");
    }

    @Test
    void test_auth_incorrect_username() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("gjihwwihfwe@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");
        boolean thrown = false;
        String errorMessage = "";

        try {
            AuthenticationResponse response = aService.authenticate(request);
        }catch (UsernameNotFoundException e) {
            thrown = true;
            errorMessage = e.getMessage();
        }

        assertThat(thrown).isTrue();
        assertThat(errorMessage).isEqualTo("Utilisateur non trouvé");
    }

    @Test
    void test_auth_route_permit_all() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        try {
            mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void test_auth_route_token() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        try {
            mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("token").exists());
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void test_auth_route_deny_all(){
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        try {
            mockMvc.perform(post("/fausseroute").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(MockMvcResultMatchers.jsonPath("token").doesNotExist());
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
