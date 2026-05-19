package fr.eni.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.RoleRepository;
import fr.eni.backend.dao.UtilisateurRepository;
import jakarta.servlet.http.Cookie;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        roleRepository.deleteAll();
        utilisateurRepository.deleteAll();

        String mdpEncoded = pEncoder.encode("JeSuisAnneLise");

        annelise = Utilisateur.builder()
                .immatriculation("ENI_25039285")
                .nom("BAILLE")
                .prenom("Anne-Lise")
                .motDePasse(mdpEncoded)
                .email("abaille@campus-eni.fr")
                .telephone("0600000000")
                .build();

        utilisateurRepository.saveAndFlush(annelise);

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
        assertThat(response.getUtilisateurDto()).isNotNull();
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
    void test_auth_route_cookie() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt_token_app_erp"));

    }

    @Test
    void test_auth_dto() throws Exception{
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");


        mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt_token_app_erp"))
                .andExpect(jsonPath("$.email").value("abaille@campus-eni.fr"))
                .andExpect(jsonPath("$.prenom").value("Anne-Lise"))
                .andExpect(jsonPath("$.nom").value("BAILLE"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"));
    }

    @Test
    void test_auth_dto_no_pwd() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");


        mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void test_auth_checkLoggedIn() throws Exception{
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");

        MvcResult result = mockMvc.perform(post("/api/auth").content(new ObjectMapper().writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getCookie("jwt_token_app_erp")).isNotNull();

        Cookie jwt = result.getResponse().getCookie("jwt_token_app_erp");

        mockMvc.perform(get("/api/auth/me").cookie(jwt)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abaille@campus-eni.fr"))
                .andExpect(jsonPath("$.prenom").value("Anne-Lise"))
                .andExpect(jsonPath("$.nom").value("BAILLE"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN")).andReturn();
    }
}
