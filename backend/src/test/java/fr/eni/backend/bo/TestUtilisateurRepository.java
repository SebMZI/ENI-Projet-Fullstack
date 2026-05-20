package fr.eni.backend.bo;

import fr.eni.backend.dao.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestUtilisateurRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private List<Role> rolesListe;
    private Adresse adresse;
    private Utilisateur utilisateur;

    @BeforeEach
    void jeuDeDonnees() {
        rolesListe = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            final Role role = Role
                    .builder()
                    .role("ROLE_" + i)
                    .build();
            rolesListe.add(role);
        }

        adresse = Adresse
                .builder()
                .rue("rue des accacias")
                .codePostal("73000")
                .ville("Bassens")
                .build();

        utilisateur = Utilisateur
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .roles(rolesListe)
                .adresse(adresse)
                .build();
    }

    @Test
    public void testUtilisateur_save() {
        final Utilisateur utilisateurDB = utilisateurRepository.save(utilisateur);

        assertThat(utilisateurDB).isNotNull();

        assertThat(utilisateurDB.getRoles()).isNotNull();
        assertThat(utilisateurDB.getRoles()).isNotEmpty();
        assertThat(utilisateurDB.getRoles().size()).isEqualTo(3);

        assertThat(utilisateurDB.getAdresse()).isNotNull();

        log.info("Utilisateur save {}", utilisateurDB);
    }

    @Test
    public void testUtilisateur_delete() {

        final Utilisateur utilisateurDB = testEntityManager.persist(utilisateur);
        testEntityManager.flush();

        assertThat(utilisateurDB).isNotNull();

        assertThat(utilisateurDB.getRoles()).isNotNull();
        assertThat(utilisateurDB.getRoles()).isNotEmpty();
        assertThat(utilisateurDB.getRoles().size()).isEqualTo(3);

        assertThat(utilisateurDB.getAdresse()).isNotNull();


        utilisateurRepository.delete(utilisateurDB);

        final Utilisateur utilisateurDB2 = testEntityManager.find(Utilisateur.class, utilisateur.getImmatriculation());
        assertNull(utilisateurDB2);

        //Verification suppression adresse
        final Adresse adresseDB2 = testEntityManager.find(Adresse.class, adresse.getId());
        assertNull(adresseDB2);

    }
}
