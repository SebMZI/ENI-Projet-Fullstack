package fr.eni.backend.service;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.dao.CoursRepository;
import fr.eni.backend.dto.CoursDTO;
import fr.eni.backend.dto.CoursRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoursServiceTest {

    @Mock
    private CoursRepository coursRepository;

    @InjectMocks
    private CoursService coursService;

    @Test
    void findAll_retourne_liste_cours() {
        Cours cours = Cours.builder().id(1).titre("HTML CSS").duree(35).build();
        when(coursRepository.findAll()).thenReturn(List.of(cours));

        List<CoursDTO> result = coursService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).isEqualTo("HTML CSS");
        assertThat(result.get(0).getDuree()).isEqualTo(35);
    }

    @Test
    void findById_retourne_cours() {
        Cours cours = Cours.builder().id(1).titre("JavaScript").duree(40).build();
        when(coursRepository.findById(1)).thenReturn(Optional.of(cours));

        CoursDTO result = coursService.findById(1);

        assertThat(result.getTitre()).isEqualTo("JavaScript");
    }

    @Test
    void findById_lance_exception_si_introuvable() {
        when(coursRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coursService.findById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cours introuvable");
    }

    @Test
    void create_sauvegarde_cours() {
        CoursRequestDTO request = new CoursRequestDTO();
        request.setTitre("React");
        request.setDuree(30);

        Cours saved = Cours.builder().id(1).titre("React").duree(30).build();
        when(coursRepository.save(any(Cours.class))).thenReturn(saved);

        CoursDTO result = coursService.create(request);

        assertThat(result.getTitre()).isEqualTo("React");
        assertThat(result.getDuree()).isEqualTo(30);
    }

    @Test
    void delete_lance_exception_si_introuvable() {
        when(coursRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> coursService.deleteById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cours introuvable avec l'id : 99");
    }
}