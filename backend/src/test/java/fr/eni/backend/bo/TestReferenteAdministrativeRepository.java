package fr.eni.backend.bo;

import fr.eni.backend.dao.ReferenteAdministrativeRepository;
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
public class TestReferenteAdministrativeRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReferenteAdministrativeRepository referenteAdministrativeRepository;

    @Test
    public void testReferenteSave() {
        ReferenteAdministrative referente = ReferenteAdministrative
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .bureau("RC")
                .build();

        final ReferenteAdministrative referenteDB = referenteAdministrativeRepository.save(referente);
        assertThat(referenteDB.getImmatriculation()).isNotNull();

    }

    @Test
    public void testFormateur_delete() {
        ReferenteAdministrative referente = ReferenteAdministrative
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .bureau("RC")
                .build();

        final ReferenteAdministrative referenteDB = testEntityManager.persist(referente);
        testEntityManager.flush();
        assertThat(referenteDB.getImmatriculation()).isNotNull();

        referenteAdministrativeRepository.delete(referenteDB);

        final ReferenteAdministrative referenteDB2 = testEntityManager.find(ReferenteAdministrative.class, referente.getImmatriculation());
        assertNull(referenteDB2);
    }
}
