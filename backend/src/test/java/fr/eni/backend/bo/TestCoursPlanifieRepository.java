package fr.eni.backend.bo;

import fr.eni.backend.dao.CoursPlanifieRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestCoursPlanifieRepository {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CoursPlanifieRepository coursPlanifiesRepository;

    private CoursPlanifie coursPlanifie;

    @BeforeEach
    void jeuDeDonnees() {
        Formateur formateur = Formateur
                .builder()
                .immatriculation("XXXXXXXXX")
                .motDePasse("XXXXXX")
                .nom("Rogerio")
                .prenom("Rodrigues")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .statut("Externe")
                .build();

        coursPlanifie = CoursPlanifie
                .builder()
                .titre("HTML/CSS")
                .duree(2)
                .dateDebut(LocalDate.of(2026, 2, 5))
                .dateFin(LocalDate.of(2026, 3, 12))
                .formateur(formateur)
                .build();

    }

    @Test
    public void testCoursPlanifies_save() {
        final CoursPlanifie coursPlanifieDB = coursPlanifiesRepository.save(coursPlanifie);

        assertThat(coursPlanifieDB.getId()).isGreaterThan(0);

        assertThat(coursPlanifieDB.getFormateur()).isNotNull();
        assertThat(coursPlanifieDB.getFormateur().getImmatriculation()).isEqualTo(coursPlanifie.getFormateur().getImmatriculation());

        log.info(coursPlanifieDB.toString());
    }

    @Test
    public void testCoursPlanifies_delete() {
        final CoursPlanifie coursPlanifieDB = testEntityManager.persist(coursPlanifie);
        testEntityManager.flush();

        assertThat(coursPlanifieDB.getId()).isGreaterThan(0);
        assertThat(coursPlanifieDB.getFormateur()).isNotNull();
        assertThat(coursPlanifieDB.getFormateur().getImmatriculation()).isEqualTo(coursPlanifie.getFormateur().getImmatriculation());

        Formateur formateur = coursPlanifieDB.getFormateur();
        String idFormateur = formateur.getImmatriculation();

        coursPlanifiesRepository.delete(coursPlanifie);

        final CoursPlanifie coursPlanifieDB2 = testEntityManager.find(CoursPlanifie.class, coursPlanifie.getId());
        assertNull(coursPlanifieDB2);

        //Vérification conservation du formateur
        assertThat(idFormateur).isNotNull();
        final Formateur formateurDB = testEntityManager.find(Formateur.class, idFormateur);

        assertThat(formateurDB.getImmatriculation()).isNotNull();
        assertThat(formateurDB).isNotNull();
        log.info(formateurDB.toString());


    }
}
