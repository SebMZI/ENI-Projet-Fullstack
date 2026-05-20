package fr.eni.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.dto.FiliereDTO;
import fr.eni.backend.dto.FiliereRequestDTO;
import fr.eni.backend.security.JwtAuthenticationFilter;
import fr.eni.backend.service.FiliereService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = FiliereController.class)
@AutoConfigureMockMvc(addFilters = false)
class FiliereControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FiliereService filiereService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;    

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_retourne_liste_filieres() throws Exception {
        FiliereDTO filiere = FiliereDTO.builder().id(1).nom("Développement").build();
        when(filiereService.findAll()).thenReturn(List.of(filiere));

        mockMvc.perform(get("/api/filieres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Développement"));
    }

    @Test
    void getById_retourne_filiere() throws Exception {
        FiliereDTO filiere = FiliereDTO.builder().id(1).nom("Développement").build();
        when(filiereService.findById(1)).thenReturn(filiere);

        mockMvc.perform(get("/api/filieres/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Développement"));
    }

    @Test
    void create_retourne_filiere_creee() throws Exception {
        FiliereRequestDTO request = new FiliereRequestDTO();
        request.setNom("Systèmes et Réseaux");

        FiliereDTO saved = FiliereDTO.builder().id(2).nom("Systèmes et Réseaux").build();
        when(filiereService.create(any(FiliereRequestDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/filieres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nom").value("Systèmes et Réseaux"));
    }

    @Test
    void create_avec_nom_vide_retourne_400() throws Exception {
        FiliereRequestDTO request = new FiliereRequestDTO();
        request.setNom("");

        mockMvc.perform(post("/api/filieres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_retourne_filiere_modifiee() throws Exception {
        FiliereRequestDTO request = new FiliereRequestDTO();
        request.setNom("Nouveau nom");

        FiliereDTO updated = FiliereDTO.builder().id(1).nom("Nouveau nom").build();
        when(filiereService.update(eq(1), any(FiliereRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/filieres/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Nouveau nom"));
    }

    @Test
    void delete_retourne_204() throws Exception {
        doNothing().when(filiereService).deleteById(1);

        mockMvc.perform(delete("/api/filieres/1"))
                .andExpect(status().isNoContent());

        verify(filiereService, times(1)).deleteById(1);
    }
}