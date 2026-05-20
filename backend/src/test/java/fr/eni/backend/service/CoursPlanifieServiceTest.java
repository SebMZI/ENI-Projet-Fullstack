package fr.eni.backend.service;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.bo.CoursPlanifie;
import fr.eni.backend.bo.Formateur;
import fr.eni.backend.bo.Promotion;
import fr.eni.backend.dao.CoursPlanifieRepository;
import fr.eni.backend.dao.CoursRepository;
import fr.eni.backend.dao.FormateurRepository;
import fr.eni.backend.dao.PromotionRepository;
import fr.eni.backend.dto.CoursPlanifieDTO;
import fr.eni.backend.dto.CoursPlanifieRequestDTO;
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
class CoursPlanifieServiceTest {

    @Mock
    private CoursPlanifieRepository coursPlanifieRepository;

    @Mock
    private CoursRepository coursRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private FormateurRepository formateurRepository;

    @InjectMocks
    private CoursPlanifieService coursPlanifieService;

    private Cours cours;
    private Promotion promotion;
    private Formateur formateur;
    private CoursPlanifie coursPlanifie;
    private CoursPlanifieRequestDTO requestDTO;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @BeforeEach
    void setUp() {
        dateDebut = LocalDate.of(2026, 9, 1);
        dateFin = LocalDate.of(2026, 9, 1);

        cours = Cours.builder()
                .id(1)
                .titre("HTML CSS")
                .duree(35)
                .build();

        promotion = Promotion.builder()
                .id(1)
                .nom("CDA Sept 2026")
                .build();

        formateur = Formateur.builder()
                .immatriculation("F001")
                .nom("Martin")
                .prenom("Jean")
                .build();

        coursPlanifie = CoursPlanifie.builder()
                .id(1)
                .titre("HTML CSS")
                .duree(35)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .formateur(formateur)
                .promotion(promotion)
                .build();

        requestDTO = new CoursPlanifieRequestDTO();
        requestDTO.setIdCours(1);
        requestDTO.setDateDebut(dateDebut);
        requestDTO.setDateFin(dateFin);
        requestDTO.setIdFormateur(1);
    }

    // ============================================================
    // TESTS POUR findAll
    // ============================================================

    @Test
    void findAll_retourne_liste_cours_planifies() {
        when(coursPlanifieRepository.findAll()).thenReturn(List.of(coursPlanifie));

        List<CoursPlanifieDTO> result = coursPlanifieService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("HTML CSS");
        assertThat(result.get(0).getDuree()).isEqualTo(35);
        verify(coursPlanifieRepository, times(1)).findAll();
    }

    // ============================================================
    // TESTS POUR findById
    // ============================================================

    @Test
    void findById_retourne_cours_planifie() {
        when(coursPlanifieRepository.findById(1)).thenReturn(Optional.of(coursPlanifie));

        CoursPlanifieDTO result = coursPlanifieService.findById(1);

        assertThat(result.getTitre()).isEqualTo("HTML CSS");
        assertThat(result.getDuree()).isEqualTo(35);
        verify(coursPlanifieRepository, times(1)).findById(1);
    }

    @Test
    void findById_lance_exception_si_introuvable() {
        when(coursPlanifieRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coursPlanifieService.findById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cours planifié introuvable");
    }

    // ============================================================
    // TESTS POUR findByPromotion
    // ============================================================

    @Test
    void findByPromotion_retourne_liste_cours_planifies() {
        when(coursPlanifieRepository.findByPromotionId(1)).thenReturn(List.of(coursPlanifie));

        List<CoursPlanifieDTO> result = coursPlanifieService.findByPromotion(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("HTML CSS");
        assertThat(result.get(0).getIdPromotion()).isEqualTo(1);
        verify(coursPlanifieRepository, times(1)).findByPromotionId(1);
    }

    // ============================================================
    // TESTS POUR create
    // ============================================================

    @Test
    void create_sauvegarde_cours_planifie() {
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(coursRepository.findById(1)).thenReturn(Optional.of(cours));
        when(formateurRepository.findById(1)).thenReturn(Optional.of(formateur));
        when(coursPlanifieRepository.save(any(CoursPlanifie.class))).thenReturn(coursPlanifie);
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        CoursPlanifieDTO result = coursPlanifieService.create(1, requestDTO);

        assertThat(result.getTitre()).isEqualTo("HTML CSS");
        assertThat(result.getIdPromotion()).isEqualTo(1);
        assertThat(result.getIdFormateur()).isEqualTo("F001");
        verify(coursPlanifieRepository, times(1)).save(any(CoursPlanifie.class));
    }

    @Test
    void create_lance_exception_si_promotion_introuvable() {
        when(promotionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coursPlanifieService.create(99, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Promotion introuvable");
    }

    @Test
    void create_lance_exception_si_cours_introuvable() {
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(coursRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coursPlanifieService.create(1, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cours introuvable");
    }

    @Test
    void create_lance_exception_si_formateur_introuvable() {
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(coursRepository.findById(1)).thenReturn(Optional.of(cours));
        when(formateurRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coursPlanifieService.create(1, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Formateur introuvable");
    }

    @Test
    void create_lance_exception_si_date_fin_avant_date_debut() {
        requestDTO.setDateFin(requestDTO.getDateDebut().minusDays(1));
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(coursRepository.findById(1)).thenReturn(Optional.of(cours));

        assertThatThrownBy(() -> coursPlanifieService.create(1, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La date de fin doit être après la date de début");
    }

    // ============================================================
    // TESTS POUR deleteById
    // ============================================================

    @Test
    void deleteById_supprime_cours_planifie() {
        when(coursPlanifieRepository.existsById(1)).thenReturn(true);
        doNothing().when(coursPlanifieRepository).deleteById(1);

        coursPlanifieService.deleteById(1);

        verify(coursPlanifieRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteById_lance_exception_si_introuvable() {
        when(coursPlanifieRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> coursPlanifieService.deleteById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cours planifié introuvable");
    }
}