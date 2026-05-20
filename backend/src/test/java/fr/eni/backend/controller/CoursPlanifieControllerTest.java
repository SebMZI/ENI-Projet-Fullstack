package fr.eni.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.dto.CoursPlanifieDTO;
import fr.eni.backend.dto.CoursPlanifieRequestDTO;
import fr.eni.backend.security.JwtAuthenticationFilter;
import fr.eni.backend.service.CoursPlanifieService;
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

@WebMvcTest(value = CoursPlanifieController.class)
@AutoConfigureMockMvc(addFilters = false)
class CoursPlanifieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoursPlanifieService coursPlanifieService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDate dateDebut = LocalDate.now().plusDays(10);
    private LocalDate dateFin = LocalDate.now().plusDays(15);

    @Test
    void getAll_retourne_liste() throws Exception {
        CoursPlanifieDTO dto = CoursPlanifieDTO.builder()
                .id(1)
                .titre("HTML CSS")
                .duree(35)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .idPromotion(1)
                .build();

        when(coursPlanifieService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/cours-planifies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titre").value("HTML CSS"))
                .andExpect(jsonPath("$[0].idPromotion").value(1));
    }

    @Test
    void getById_retourne_cours_planifie() throws Exception {
        CoursPlanifieDTO dto = CoursPlanifieDTO.builder()
                .id(1)
                .titre("JavaScript")
                .duree(40)
                .idPromotion(1)
                .build();

        when(coursPlanifieService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/cours-planifies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("JavaScript"))
                .andExpect(jsonPath("$.duree").value(40));
    }

    @Test
    void getByPromotion_retourne_liste() throws Exception {
        CoursPlanifieDTO dto = CoursPlanifieDTO.builder()
                .id(1)
                .titre("HTML CSS")
                .idPromotion(1)
                .build();

        when(coursPlanifieService.findByPromotion(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/cours-planifies/promotion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPromotion").value(1));
    }

    @Test
    void create_retourne_cours_cree() throws Exception {
        CoursPlanifieRequestDTO request = new CoursPlanifieRequestDTO();
        request.setIdCours(1);
        request.setDateDebut(dateDebut);
        request.setDateFin(dateFin);

        CoursPlanifieDTO saved = CoursPlanifieDTO.builder()
                .id(1)
                .titre("HTML CSS")
                .duree(35)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .idPromotion(1)
                .build();

        when(coursPlanifieService.create(eq(1), any(CoursPlanifieRequestDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/cours-planifies/promotion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("HTML CSS"))
                .andExpect(jsonPath("$.idPromotion").value(1));
    }

    @Test
    void create_avec_dates_invalides_retourne_400() throws Exception {
        CoursPlanifieRequestDTO request = new CoursPlanifieRequestDTO();
        request.setIdCours(1);
        request.setDateDebut(null);  // @NotNull
        request.setDateFin(dateFin);

        mockMvc.perform(post("/api/cours-planifies/promotion/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_retourne_204() throws Exception {
        doNothing().when(coursPlanifieService).deleteById(1);

        mockMvc.perform(delete("/api/cours-planifies/1"))
                .andExpect(status().isNoContent());

        verify(coursPlanifieService, times(1)).deleteById(1);
    }
}