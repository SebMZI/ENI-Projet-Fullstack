package fr.eni.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eni.backend.bo.Eleve; 
import fr.eni.backend.dto.PromotionDTO;
import fr.eni.backend.dto.PromotionRequestDTO;
import fr.eni.backend.security.JwtAuthenticationFilter;
import fr.eni.backend.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import fr.eni.backend.bo.Promotion;
import fr.eni.backend.dto.EleveDTO;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PromotionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromotionService promotionService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDate futureDate = LocalDate.now().plusMonths(1);
    private LocalDate laterDate = futureDate.plusMonths(3);

    @Test
    void getAll_retourne_liste_promotions() throws Exception {
        PromotionDTO dto = PromotionDTO.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .idCursus(1)
                .build();

        when(promotionService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("CDA Sept 2026"))
                .andExpect(jsonPath("$[0].idCursus").value(1));
    }

    @Test
    void getById_retourne_promotion() throws Exception {
        PromotionDTO dto = PromotionDTO.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .idCursus(1)
                .build();

        when(promotionService.findById(1)).thenReturn(dto);

        mockMvc.perform(get("/api/promotions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("CDA Sept 2026"))
                .andExpect(jsonPath("$.idCursus").value(1));
    }

    @Test
    void create_retourne_promotion_creee() throws Exception {
        PromotionRequestDTO request = new PromotionRequestDTO();
        request.setNom("CDA Sept 2026");
        request.setDateDebut(futureDate);
        request.setDateFin(laterDate);
        request.setIdCursus(1);

        PromotionDTO saved = PromotionDTO.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .idCursus(1)
                .build();

        when(promotionService.create(any(PromotionRequestDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("CDA Sept 2026"))
                .andExpect(jsonPath("$.idCursus").value(1));
    }

    @Test
    void create_avec_nom_vide_retourne_400() throws Exception {
        PromotionRequestDTO request = new PromotionRequestDTO();
        request.setNom("");
        request.setDateDebut(futureDate);
        request.setDateFin(laterDate);
        request.setIdCursus(1);

        mockMvc.perform(post("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_retourne_promotion_modifiee() throws Exception {
        PromotionRequestDTO request = new PromotionRequestDTO();
        request.setNom("CDA Sept 2026 modifié");
        request.setDateDebut(futureDate);
        request.setDateFin(laterDate);
        request.setIdCursus(1);

        PromotionDTO updated = PromotionDTO.builder()
                .id(1)
                .nom("CDA Sept 2026 modifié")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .idCursus(1)
                .build();

        when(promotionService.update(eq(1), any(PromotionRequestDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/promotions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("CDA Sept 2026 modifié"));
    }

    @Test
    void delete_retourne_204() throws Exception {
        doNothing().when(promotionService).deleteById(1);

        mockMvc.perform(delete("/api/promotions/1"))
                .andExpect(status().isNoContent());

        verify(promotionService, times(1)).deleteById(1);
    }


        @Test
        void inscrireEleve_retourne_promotion_modifiee() throws Exception {
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setNom("CDA Sept 2026");
        promotion.setDateDebut(futureDate);
        promotion.setDateFin(laterDate);

        PromotionDTO dto = PromotionDTO.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .idCursus(1)
                .build();

        when(promotionService.inscrireEleve(1, "E001")).thenReturn(promotion);
        when(promotionService.convertToDTO(promotion)).thenReturn(dto);

        mockMvc.perform(post("/api/promotions/1/eleves/E001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("CDA Sept 2026"));
        }

        @Test
        void desinscrireEleve_retourne_promotion_modifiee() throws Exception {
        Promotion promotion = new Promotion();
        promotion.setId(1);

        PromotionDTO dto = PromotionDTO.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .idCursus(1)
                .build();

        when(promotionService.desinscrireEleve(1, "E001")).thenReturn(promotion);
        when(promotionService.convertToDTO(promotion)).thenReturn(dto);

        mockMvc.perform(delete("/api/promotions/1/eleves/E001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void getEleves_retourne_liste_eleves() throws Exception {
        EleveDTO eleveDTO = new EleveDTO("E001", "Dupont", "Jean", "jean@campus-eni.fr");
        when(promotionService.getElevesByPromotion(1)).thenReturn(List.of(eleveDTO));

        mockMvc.perform(get("/api/promotions/1/eleves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].immatriculation").value("E001"));
        }
}