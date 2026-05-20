package fr.eni.backend.bo;

import fr.eni.backend.dao.AdministrateurRepository;
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
public class TestAdministrateurRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Test
    public void testAdministrateurSave() {
        Administrateur administrateur = Administrateur
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .service("Administration")
                .build();

        final Administrateur referenteDB = administrateurRepository.save(administrateur);
        assertThat(referenteDB.getImmatriculation()).isNotNull();

    }

    @Test
    public void testFormateur_delete() {
        Administrateur administrateur = Administrateur
                .builder()
                .immatriculation(("XXXXXXXXXX"))
                .motDePasse("XXXXXXXXX")
                .nom("Test")
                .prenom("TestPrenom")
                .email("email@campus-eni.fr")
                .telephone("XXXXXXXXXX")
                .dateCreation(LocalDate.now())
                .service("Administration")
                .build();

        final Administrateur administrateurDB = testEntityManager.persist(administrateur);
        testEntityManager.flush();
        assertThat(administrateurDB.getImmatriculation()).isNotNull();

        administrateurRepository.delete(administrateurDB);

        final Administrateur administrateurDB2 = testEntityManager.find(Administrateur.class, administrateur.getImmatriculation());
        assertNull(administrateurDB2);
    }
}
