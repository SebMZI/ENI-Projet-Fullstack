package fr.eni.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.dto.CoursDTO;
import fr.eni.backend.dto.CoursRequestDTO;
import fr.eni.backend.service.CoursService;
import fr.eni.backend.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = CoursController.class)
@AutoConfigureMockMvc(addFilters = false)
class CoursControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoursService coursService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;    

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_retourne_liste_cours() throws Exception {
        CoursDTO cours = CoursDTO.builder().id(1).titre("HTML CSS").duree(35).build();
        when(coursService.findAll()).thenReturn(List.of(cours));

        mockMvc.perform(get("/api/cours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("HTML CSS"))
                .andExpect(jsonPath("$[0].duree").value(35));
    }

    @Test
    void getById_retourne_cours() throws Exception {
        CoursDTO cours = CoursDTO.builder().id(1).titre("JavaScript").duree(40).build();
        when(coursService.findById(1)).thenReturn(cours);

        mockMvc.perform(get("/api/cours/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("JavaScript"))
                .andExpect(jsonPath("$.duree").value(40));
    }

    @Test
    void create_retourne_cours_cree() throws Exception {
        CoursRequestDTO request = new CoursRequestDTO();
        request.setTitre("React");
        request.setDuree(30);
        request.setIdCursus(1);

        CoursDTO saved = CoursDTO.builder().id(1).titre("React").duree(30).idCursus(1).build();
        when(coursService.create(any(CoursRequestDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/cours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("React"))
                .andExpect(jsonPath("$.duree").value(30));
    }

    @Test
    void create_avec_titre_vide_retourne_400() throws Exception {
        CoursRequestDTO request = new CoursRequestDTO();
        request.setTitre("");
        request.setDuree(30);

        mockMvc.perform(post("/api/cours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_retourne_cours_modifie() throws Exception {
        CoursRequestDTO request = new CoursRequestDTO();
        request.setTitre("React Avancé");
        request.setDuree(45);
        request.setIdCursus(1);
        
        CoursDTO updated = CoursDTO.builder().id(1).titre("React Avancé").duree(45).build();
        when(coursService.update(eq(1), any(CoursRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/cours/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("React Avancé"));
    }

    @Test
    void delete_retourne_204() throws Exception {
        doNothing().when(coursService).deleteById(1);

        mockMvc.perform(delete("/api/cours/1"))
                .andExpect(status().isNoContent());

        verify(coursService, times(1)).deleteById(1);
    }
}