package fr.eni.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.dto.UtilisateurDTO;
import fr.eni.backend.dto.UtilisateurRequestDTO;
import fr.eni.backend.security.JwtAuthenticationFilter;
import fr.eni.backend.service.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UtilisateurController.class)
@AutoConfigureMockMvc(addFilters = false)
class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UtilisateurService utilisateurService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_retourne_liste_utilisateurs() throws Exception {
        UtilisateurDTO dto = UtilisateurDTO.builder()
                .immatriculation("E001")
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@campus-eni.fr")
                .roles(List.of("ELEVE"))
                .dateCreation(LocalDate.now())
                .build();

        when(utilisateurService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].immatriculation").value("E001"))
                .andExpect(jsonPath("$[0].roles[0]").value("ELEVE"));
    }

    @Test
    void getById_retourne_utilisateur() throws Exception {
        UtilisateurDTO dto = UtilisateurDTO.builder()
                .immatriculation("F001")
                .nom("Martin")
                .prenom("Sophie")
                .email("sophie@campus-eni.fr")
                .roles(List.of("FORMATEUR"))
                .statut("Permanent")
                .build();

        when(utilisateurService.findById("F001")).thenReturn(dto);

        mockMvc.perform(get("/api/utilisateurs/F001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.immatriculation").value("F001"))
                .andExpect(jsonPath("$.statut").value("Permanent"));
    }

    @Test
    void create_retourne_utilisateur_cree() throws Exception {
        UtilisateurRequestDTO request = new UtilisateurRequestDTO();
        request.setImmatriculation("E001");
        request.setNom("Dupont");
        request.setPrenom("Jean");
        request.setEmail("jean@campus-eni.fr");
        request.setMotDePasse("password123");
        request.setRoles(List.of("ELEVE"));
        request.setEmailPersonnel("jean@gmail.com");

        UtilisateurDTO saved = UtilisateurDTO.builder()
                .immatriculation("E001")
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@campus-eni.fr")
                .roles(List.of("ELEVE"))
                .emailPersonnel("jean@gmail.com")
                .dateCreation(LocalDate.now())
                .build();

        when(utilisateurService.create(any(UtilisateurRequestDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.immatriculation").value("E001"))
                .andExpect(jsonPath("$.emailPersonnel").value("jean@gmail.com"));
    }

    @Test
    void create_avec_champs_invalides_retourne_400() throws Exception {
        UtilisateurRequestDTO request = new UtilisateurRequestDTO();
        request.setImmatriculation("");
        request.setNom("");
        request.setPrenom("");
        request.setEmail("mauvais-email");
        request.setMotDePasse("");
        request.setRoles(List.of(""));

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_retourne_utilisateur_modifie() throws Exception {
        UtilisateurRequestDTO request = new UtilisateurRequestDTO();
        request.setImmatriculation("F001");
        request.setNom("Martin");
        request.setPrenom("Sophie");
        request.setEmail("sophie@campus-eni.fr");
        request.setMotDePasse("newPass");
        request.setRoles(List.of("FORMATEUR"));
        request.setStatut("Vacataire");

        UtilisateurDTO updated = UtilisateurDTO.builder()
                .immatriculation("F001")
                .nom("Martin")
                .prenom("Sophie")
                .email("sophie@campus-eni.fr")
                .roles(List.of("FORMATEUR"))
                .statut("Vacataire")
                .build();

        when(utilisateurService.update(eq("F001"), any(UtilisateurRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/utilisateurs/F001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("Vacataire"));
    }

    @Test
    void delete_retourne_204() throws Exception {
        doNothing().when(utilisateurService).deleteById("E001");

        mockMvc.perform(delete("/api/utilisateurs/E001"))
                .andExpect(status().isNoContent());

        verify(utilisateurService, times(1)).deleteById("E001");
    }
}