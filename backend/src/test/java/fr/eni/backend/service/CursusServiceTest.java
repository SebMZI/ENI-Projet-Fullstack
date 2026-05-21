package fr.eni.backend.service;

import fr.eni.backend.bo.Cursus;
import fr.eni.backend.bo.Filiere;
import fr.eni.backend.dao.CursusRepository;
import fr.eni.backend.dao.FiliereRepository;
import fr.eni.backend.dto.CursusDTO;
import fr.eni.backend.dto.CursusRequestDTO;
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
class CursusServiceTest {

    @Mock
    private CursusRepository cursusRepository;

    @Mock
    private FiliereRepository filiereRepository;

    @InjectMocks
    private CursusService cursusService;

    @Test
    void findAll_retourne_liste_cursus() {
        Cursus cursus = Cursus.builder().id(1).intitule("CDA").build();
        when(cursusRepository.findAll()).thenReturn(List.of(cursus));

        List<CursusDTO> result = cursusService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIntitule()).isEqualTo("CDA");
    }

    @Test
    void findById_retourne_cursus() {
        Cursus cursus = Cursus.builder().id(1).intitule("ASR").build();
        when(cursusRepository.findById(1)).thenReturn(Optional.of(cursus));

        CursusDTO result = cursusService.findById(1);

        assertThat(result.getIntitule()).isEqualTo("ASR");
    }

    @Test
    void findById_lance_exception_si_introuvable() {
        when(cursusRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursusService.findById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cursus introuvable");
    }

    @Test
    void create_sauvegarde_cursus() {
        Filiere filiere = Filiere.builder().id(1).nom("Développement").build();
        when(filiereRepository.findById(1)).thenReturn(Optional.of(filiere));

        CursusRequestDTO request = new CursusRequestDTO();
        request.setIntitule("D2WM");
        request.setIdFiliere(1);

        Cursus saved = Cursus.builder().id(1).intitule("D2WM").fieldId(1).build();
        when(cursusRepository.save(any(Cursus.class))).thenReturn(saved);

        CursusDTO result = cursusService.create(request);

        assertThat(result.getIntitule()).isEqualTo("D2WM");
        verify(cursusRepository, times(1)).save(any(Cursus.class));
    }

    @Test
    void delete_supprime_cursus() {
        when(cursusRepository.existsById(1)).thenReturn(true);

        cursusService.deleteById(1);

        verify(cursusRepository, times(1)).deleteById(1);
    }
}