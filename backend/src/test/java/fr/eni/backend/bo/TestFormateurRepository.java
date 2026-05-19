package fr.eni.backend.bo;

import fr.eni.backend.dao.EleveRepository;
import fr.eni.backend.dao.FormateurRepository;
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
public class TestFormateurRepository {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private FormateurRepository formateurRepository;

    @Test
    public void testFormateurSave() {
        Formateur formateur = Formateur
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .statut("Externe")
                .build();

        final Formateur formateurDB = formateurRepository.save(formateur);
        assertThat(formateurDB.getImmatriculation()).isNotNull();

    }

    @Test
    public void testFormateur_delete() {
        Formateur formateur  = Formateur
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .statut("Externe")
                .build();

        final Formateur formateurDB = testEntityManager.persist(formateur);
        testEntityManager.flush();
        assertThat(formateurDB.getImmatriculation()).isNotNull();

        formateurRepository.delete(formateurDB);

        final Formateur formateurDB2 = testEntityManager.find(Formateur.class, formateur.getImmatriculation());
        assertNull(formateurDB2);
    }
}
