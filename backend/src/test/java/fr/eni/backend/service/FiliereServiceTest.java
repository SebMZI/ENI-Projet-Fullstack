package fr.eni.backend.service;

import fr.eni.backend.bo.Filiere;
import fr.eni.backend.dao.FiliereRepository;
import fr.eni.backend.dto.FiliereDTO;
import fr.eni.backend.dto.FiliereRequestDTO;
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
class FiliereServiceTest {


    @Mock
    private FiliereRepository filiereRepository;

    
    @InjectMocks
    private FiliereService filiereService;


    // Test 1 : findAll retourne une liste 
    @Test
    void findAll_retourne_liste_filieres() {

        // ARRANGE 
        Filiere filiere = Filiere.builder().id(1).nom("Développement").build();

        when(filiereRepository.findAll()).thenReturn(List.of(filiere));

        // ACT  
        List<FiliereDTO> result = filiereService.findAll();

        // ASSERT 
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Développement");
        assertThat(result.get(0).getId()).isEqualTo(1);
    }


    //  Test 2 : Vérifier que findById(1) retourne la bonne filiere
    @Test
    void findById_retourne_filiere() {

        // ARRANGE
        Filiere filiere = Filiere.builder().id(1).nom("Développement").build();
        when(filiereRepository.findById(1)).thenReturn(Optional.of(filiere));

        // ACT
        FiliereDTO result = filiereService.findById(1);

        // ASSERT
        assertThat(result.getNom()).isEqualTo("Développement");
        assertThat(result.getId()).isEqualTo(1);
    }

    //  Test 3 : findById lance exception si introuvable 
    @Test
    void findById_lance_exception_si_introuvable() {
        // ARRANGE — simule un id qui n'existe pas
        when(filiereRepository.findById(99)).thenReturn(Optional.empty());

        // ASSERT — vérifie que ça lance bien une exception
        assertThatThrownBy(() -> filiereService.findById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Filière introuvable");
    }

    //  Test 4 : create sauvegarde et retourne la filière 
    @Test
    void create_sauvegarde_et_retourne_filiere() {
        // ARRANGE
        FiliereRequestDTO request = new FiliereRequestDTO();
        request.setNom("Systèmes et Réseaux");

        Filiere saved = Filiere.builder().id(2).nom("Systèmes et Réseaux").build();
        when(filiereRepository.save(any(Filiere.class))).thenReturn(saved);

        // ACT
        FiliereDTO result = filiereService.create(request);

        // ASSERT
        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getNom()).isEqualTo("Systèmes et Réseaux");
        verify(filiereRepository, times(1)).save(any(Filiere.class));
    }

    // ─── Test 5 : update modifie le nom 
    @Test
    void update_modifie_nom_filiere() {
        // ARRANGE
        Filiere existing = Filiere.builder().id(1).nom("Ancien nom").build();
        when(filiereRepository.findById(1)).thenReturn(Optional.of(existing));
        when(filiereRepository.save(any(Filiere.class))).thenReturn(existing);

        FiliereRequestDTO request = new FiliereRequestDTO();
        request.setNom("Nouveau nom");

        // ACT
        FiliereDTO result = filiereService.update(1, request);

        // ASSERT
        assertThat(result.getNom()).isEqualTo("Nouveau nom");
    }

    //  Test 6 : delete supprime la filière 
    @Test
    void delete_supprime_filiere() {
        // ARRANGE
        when(filiereRepository.existsById(1)).thenReturn(true);

        // ACT
        filiereService.deleteById(1);

        // ASSERT — vérifie que deleteById a bien été appelé
        verify(filiereRepository, times(1)).deleteById(1);
    }

    //  Test 7 : delete lance exception si introuvable 
    @Test
    void delete_lance_exception_si_introuvable() {
        // ARRANGE
        when(filiereRepository.existsById(99)).thenReturn(false);

        // ASSERT
        assertThatThrownBy(() -> filiereService.deleteById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Filière introuvable avec l'id : 99");
    }
}