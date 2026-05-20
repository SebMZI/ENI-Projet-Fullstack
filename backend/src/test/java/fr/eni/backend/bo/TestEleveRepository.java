package fr.eni.backend.bo;

import fr.eni.backend.dao.EleveRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestEleveRepository {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EleveRepository eleveRepository;

    @Test
    public void testEleveSave() {
        Eleve eleve = Eleve
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .emailPersonnel("email@perso.fr")
                .dateInscription(LocalDate.now())
                .build();

        final Eleve eleveDB = testEntityManager.persist(eleve);
        testEntityManager.flush();
        assertThat(eleveDB.getImmatriculation()).isNotNull();

    }

    @Test
    public void testEleve_delete() {
        Eleve eleve = Eleve
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .emailPersonnel("email@perso.fr")
                .dateInscription(LocalDate.now())
                .build();

        final Eleve eleveDB = eleveRepository.save(eleve);
        assertThat(eleveDB.getImmatriculation()).isNotNull();

        eleveRepository.delete(eleveDB);

        final Eleve eleverDB2 = testEntityManager.find(Eleve.class, eleve.getImmatriculation());
        assertNull(eleverDB2);
    }
}
