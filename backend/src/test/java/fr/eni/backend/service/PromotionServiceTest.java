package fr.eni.backend.service;
import fr.eni.backend.bo.Eleve;
import fr.eni.backend.dto.EleveDTO;
import fr.eni.backend.dao.EleveRepository;
import fr.eni.backend.bo.Cursus;
import fr.eni.backend.bo.Promotion;
import fr.eni.backend.dao.CursusRepository;
import fr.eni.backend.dao.PromotionRepository;
import fr.eni.backend.dto.PromotionDTO;
import fr.eni.backend.dto.PromotionRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private CursusRepository cursusRepository;

    @InjectMocks
    private PromotionService promotionService;

    @Mock
    private EleveRepository eleveRepository;

    @Mock
    private Eleve eleve;

    private Cursus cursus;
    private Promotion promotion;
    private PromotionRequestDTO requestDTO;
    private LocalDate futureDate;
    private LocalDate laterDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDate.now().plusMonths(1);
        laterDate = futureDate.plusMonths(3);

        cursus = Cursus.builder().id(1).intitule("CDA").build();

        promotion = Promotion.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .cursus(cursus)
                .build();

        requestDTO = new PromotionRequestDTO();
        requestDTO.setNom("CDA Sept 2026");
        requestDTO.setDateDebut(futureDate);
        requestDTO.setDateFin(laterDate);
        requestDTO.setIdCursus(1);
    }

    @Test
    void findAll_retourne_liste_promotions() {
        when(promotionRepository.findAll()).thenReturn(List.of(promotion));

        List<PromotionDTO> result = promotionService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("CDA Sept 2026");
        assertThat(result.get(0).getIdCursus()).isEqualTo(1);
    }

    @Test
    void findById_retourne_promotion() {
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));

        PromotionDTO result = promotionService.findById(1);

        assertThat(result.getNom()).isEqualTo("CDA Sept 2026");
        assertThat(result.getIdCursus()).isEqualTo(1);
    }

    @Test
    void findById_lance_exception_si_introuvable() {
        when(promotionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.findById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Promotion introuvable");
    }

    @Test
    void create_sauvegarde_promotion() {
        when(cursusRepository.findById(1)).thenReturn(Optional.of(cursus));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        PromotionDTO result = promotionService.create(requestDTO);

        assertThat(result.getNom()).isEqualTo("CDA Sept 2026");
        assertThat(result.getIdCursus()).isEqualTo(1);
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    void create_lance_exception_si_date_debut_passee() {
        requestDTO.setDateDebut(LocalDate.now().minusDays(1));

        assertThatThrownBy(() -> promotionService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La date de début doit être dans le futur");
    }

    @Test
    void create_lance_exception_si_date_fin_avant_date_debut() {
        requestDTO.setDateFin(requestDTO.getDateDebut().minusDays(1));

        assertThatThrownBy(() -> promotionService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La date de fin doit être après la date de début");
    }

    @Test
    void create_lance_exception_si_cursus_introuvable() {
        when(cursusRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cursus introuvable");
    }

    @Test
    void update_modifie_promotion() {
        Promotion existing = Promotion.builder()
                .id(1)
                .nom("Ancien nom")
                .dateDebut(futureDate)
                .dateFin(laterDate)
                .cursus(cursus)
                .build();

        when(promotionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(cursusRepository.findById(1)).thenReturn(Optional.of(cursus));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(existing);

        PromotionRequestDTO updateRequest = new PromotionRequestDTO();
        updateRequest.setNom("Nouveau nom");
        updateRequest.setDateDebut(futureDate);
        updateRequest.setDateFin(laterDate);
        updateRequest.setIdCursus(1);

        PromotionDTO result = promotionService.update(1, updateRequest);

        assertThat(result.getNom()).isEqualTo("Nouveau nom");
    }

    @Test
    void delete_supprime_promotion() {
        when(promotionRepository.existsById(1)).thenReturn(true);
        doNothing().when(promotionRepository).deleteById(1);

        // On appelle la méthode et on vérifie qu'aucune exception n'est levée
        promotionService.deleteById(1);

        verify(promotionRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_lance_exception_si_introuvable() {
        when(promotionRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> promotionService.deleteById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Promotion introuvable avec l'id : 99");
    }


 // ===== INSCRIPTION ELEVE =====

    @Test
    void inscrireEleve_ajoute_eleve_a_promotion() {
        Eleve eleve = Eleve.builder()
                .immatriculation("E001")
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@campus-eni.fr")
                .build();

        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(eleveRepository.findById("E001")).thenReturn(Optional.of(eleve));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        Promotion result = promotionService.inscrireEleve(1, "E001");

        assertThat(result.getEleves()).contains(eleve);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    void inscrireEleve_promotion_introuvable_lance_exception() {
        when(promotionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.inscrireEleve(99, "E001"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Promotion introuvable");
    }

    @Test
    void inscrireEleve_eleve_introuvable_lance_exception() {
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(eleveRepository.findById("INCONNU")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> promotionService.inscrireEleve(1, "INCONNU"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Élève introuvable");
    }

    @Test
    void getElevesByPromotion_retourne_liste_eleves() {
        Eleve eleve = Eleve.builder()
                .immatriculation("E001")
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@campus-eni.fr")
                .build();
        promotion.setEleves(List.of(eleve));
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));

        List<EleveDTO> result = promotionService.getElevesByPromotion(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getImmatriculation()).isEqualTo("E001");
    }
}