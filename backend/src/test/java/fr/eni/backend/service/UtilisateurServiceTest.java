package fr.eni.backend.service;

import fr.eni.backend.bo.*;
import fr.eni.backend.dao.*;
import fr.eni.backend.dto.UtilisateurDTO;
import fr.eni.backend.dto.UtilisateurRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private EleveRepository eleveRepository;

    @Mock
    private FormateurRepository formateurRepository;

    @Mock
    private ReferenteAdministrativeRepository referenteRepository;

    @Mock
    private AdministrateurRepository administrateurRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Eleve eleve;
    private Formateur formateur;
    private UtilisateurRequestDTO requestDTO;
    private Role roleEleve;
    private Role roleFormateur;

    @BeforeEach
    void setUp() {
        roleEleve = Role.builder().id(1).role("ELEVE").build();
        roleFormateur = Role.builder().id(2).role("FORMATEUR").build();

        eleve = Eleve.builder()
                .immatriculation("E001")
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@campus-eni.fr")
                .motDePasse("encodedPass")
                .telephone("0102030405")
                .dateCreation(LocalDate.now())
                .emailPersonnel("jean.dupont@gmail.com")
                .dateInscription(LocalDate.now())
                .roles(new ArrayList<>(List.of(roleEleve)))
                .build();

        formateur = Formateur.builder()
                .immatriculation("F001")
                .nom("Martin")
                .prenom("Sophie")
                .email("sophie@campus-eni.fr")
                .motDePasse("encodedPass")
                .telephone("0601020304")
                .dateCreation(LocalDate.now())
                .statut("Permanent")
                .roles(new ArrayList<>(List.of(roleFormateur)))
                .build();

        requestDTO = new UtilisateurRequestDTO();
        requestDTO.setImmatriculation("E001");
        requestDTO.setNom("Dupont");
        requestDTO.setPrenom("Jean");
        requestDTO.setEmail("jean.dupont@campus-eni.fr");
        requestDTO.setMotDePasse("password123");
        requestDTO.setTelephone("0102030405");
        requestDTO.setRole("ELEVE");
        requestDTO.setEmailPersonnel("jean.dupont@gmail.com");
    }

    // ──── findAll ────
    @Test
    void findAll_retourne_liste_utilisateurs() {
        when(utilisateurRepository.findAll()).thenReturn(List.of(eleve, formateur));

        List<UtilisateurDTO> result = utilisateurService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRole()).isEqualTo("ELEVE");
        assertThat(result.get(1).getRole()).isEqualTo("FORMATEUR");
    }

    // ──── findById ────
    @Test
    void findById_retourne_utilisateur() {
        when(utilisateurRepository.findById("E001")).thenReturn(Optional.of(eleve));

        UtilisateurDTO result = utilisateurService.findById("E001");

        assertThat(result.getNom()).isEqualTo("Dupont");
        assertThat(result.getRole()).isEqualTo("ELEVE");
        assertThat(result.getEmailPersonnel()).isEqualTo("jean.dupont@gmail.com");
    }

    @Test
    void findById_lance_exception_si_introuvable() {
        when(utilisateurRepository.findById("INCONNU")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilisateurService.findById("INCONNU"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur introuvable");
    }

    // ──── create (ELEVE) ────
    @Test
    void create_eleve_sauvegarde_et_retourne_dto() {
        when(utilisateurRepository.existsById("E001")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(eleveRepository.save(any(Eleve.class))).thenReturn(eleve);
        when(roleRepository.findByRole("ELEVE")).thenReturn(Optional.of(roleEleve));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(eleve);

        UtilisateurDTO result = utilisateurService.create(requestDTO);

        assertThat(result.getImmatriculation()).isEqualTo("E001");
        assertThat(result.getRole()).isEqualTo("ELEVE");
        assertThat(result.getEmailPersonnel()).isEqualTo("jean.dupont@gmail.com");
        verify(eleveRepository, times(1)).save(any(Eleve.class));
        verify(roleRepository, times(1)).findByRole("ELEVE");
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void create_lance_exception_si_immatriculation_existe() {
        when(utilisateurRepository.existsById("E001")).thenReturn(true);

        assertThatThrownBy(() -> utilisateurService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Immatriculation déjà utilisée");
    }

    @Test
    void create_lance_exception_si_role_inconnu() {
        requestDTO.setRole("INCONNU");
        when(utilisateurRepository.existsById("E001")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");

        assertThatThrownBy(() -> utilisateurService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Rôle inconnu : INCONNU");
    }

    @Test
    void create_cree_role_si_inexistant() {
        when(utilisateurRepository.existsById("E001")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(eleveRepository.save(any(Eleve.class))).thenReturn(eleve);
        when(roleRepository.findByRole("ELEVE")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(roleEleve);
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(eleve);

        UtilisateurDTO result = utilisateurService.create(requestDTO);

        assertThat(result.getRole()).isEqualTo("ELEVE");
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    // ──── create (FORMATEUR) ────
    @Test
    void create_formateur_sauvegarde_et_retourne_dto() {
        requestDTO.setImmatriculation("F001");
        requestDTO.setRole("FORMATEUR");
        requestDTO.setStatut("Permanent");
        requestDTO.setEmailPersonnel(null);

        when(utilisateurRepository.existsById("F001")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(formateurRepository.save(any(Formateur.class))).thenReturn(formateur);
        when(roleRepository.findByRole("FORMATEUR")).thenReturn(Optional.of(roleFormateur));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(formateur);

        UtilisateurDTO result = utilisateurService.create(requestDTO);

        assertThat(result.getImmatriculation()).isEqualTo("F001");
        assertThat(result.getRole()).isEqualTo("FORMATEUR");
        assertThat(result.getStatut()).isEqualTo("Permanent");
        verify(formateurRepository, times(1)).save(any(Formateur.class));
        verify(roleRepository, times(1)).findByRole("FORMATEUR");
    }

    // ──── update ────
    @Test
    void update_modifie_utilisateur() {
        when(utilisateurRepository.findById("F001")).thenReturn(Optional.of(formateur));
        when(passwordEncoder.encode("newPass")).thenReturn("newEncodedPass");
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(formateur);
        when(formateurRepository.save(any(Formateur.class))).thenReturn(formateur);

        UtilisateurRequestDTO updateRequest = new UtilisateurRequestDTO();
        updateRequest.setImmatriculation("F001");
        updateRequest.setNom("Martin");
        updateRequest.setPrenom("Sophie");
        updateRequest.setEmail("sophie@campus-eni.fr");
        updateRequest.setMotDePasse("newPass");
        updateRequest.setRole("FORMATEUR");
        updateRequest.setStatut("Vacataire");

        UtilisateurDTO result = utilisateurService.update("F001", updateRequest);

        assertThat(result.getStatut()).isEqualTo("Vacataire");
        verify(formateurRepository, times(1)).save(any(Formateur.class));
    }

    @Test
    void update_lance_exception_si_utilisateur_introuvable() {
        when(utilisateurRepository.findById("INCONNU")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilisateurService.update("INCONNU", requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur introuvable");
    }

    // ──── delete ────
    @Test
    void delete_supprime_utilisateur() {
        when(utilisateurRepository.findById("E001")).thenReturn(Optional.of(eleve));
        doNothing().when(utilisateurRepository).delete(eleve); 

        utilisateurService.deleteById("E001");

        verify(utilisateurRepository, times(1)).delete(eleve);  
        verify(utilisateurRepository, never()).save(any(Utilisateur.class)); 
    }

    @Test
    void delete_lance_exception_si_utilisateur_introuvable() {
        when(utilisateurRepository.findById("INCONNU")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> utilisateurService.deleteById("INCONNU"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur introuvable");
    }
}