package fr.eni.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.dto.CursusDTO;
import fr.eni.backend.dto.CursusRequestDTO;
import fr.eni.backend.service.CursusService;
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

@WebMvcTest(value = CursusController.class)
@AutoConfigureMockMvc(addFilters = false)
class CursusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursusService cursusService;
    
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;    

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_retourne_liste_cursus() throws Exception {
        CursusDTO cursus = CursusDTO.builder().id(1).intitule("CDA").build();
        when(cursusService.findAll()).thenReturn(List.of(cursus));

        mockMvc.perform(get("/api/cursus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].intitule").value("CDA"));
    }

    @Test
    void getById_retourne_cursus() throws Exception {
        CursusDTO cursus = CursusDTO.builder().id(1).intitule("ASR").build();
        when(cursusService.findById(1)).thenReturn(cursus);

        mockMvc.perform(get("/api/cursus/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule").value("ASR"));
    }

    @Test
    void create_retourne_cursus_cree() throws Exception {
        CursusRequestDTO request = new CursusRequestDTO();
        request.setIntitule("D2WM");

        CursusDTO saved = CursusDTO.builder().id(1).intitule("D2WM").build();
        when(cursusService.create(any(CursusRequestDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/cursus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule").value("D2WM"));
    }

    @Test
    void create_avec_intitule_vide_retourne_400() throws Exception {
        CursusRequestDTO request = new CursusRequestDTO();
        request.setIntitule("");

        mockMvc.perform(post("/api/cursus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_retourne_cursus_modifie() throws Exception {
        CursusRequestDTO request = new CursusRequestDTO();
        request.setIntitule("CDA Avancé");

        CursusDTO updated = CursusDTO.builder().id(1).intitule("CDA Avancé").build();
        when(cursusService.update(eq(1), any(CursusRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/cursus/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.intitule").value("CDA Avancé"));
    }

    @Test
    void delete_retourne_204() throws Exception {
        doNothing().when(cursusService).deleteById(1);

        mockMvc.perform(delete("/api/cursus/1"))
                .andExpect(status().isNoContent());

        verify(cursusService, times(1)).deleteById(1);
    }
}