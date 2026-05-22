package fr.eni.backend.bo;

import fr.eni.backend.dao.AdresseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestAdresseRepository {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AdresseRepository adresseRepository;

    private Adresse adresse;

    @Test
    public void testRole_save() {
        adresse = Adresse
                .builder()
                .rue("rue des accacias")
                .codePostal("73000")
                .ville("Bassens")
                .build();

        final Adresse adresseDB = adresseRepository.save(adresse);
        assertThat(adresseDB).isNotNull();
    }

    @Test
    public void testRole_delete() {
        adresse = Adresse
                .builder()
                .rue("rue des accacias")
                .codePostal("73000")
                .ville("Bassens")
                .build();

        final Adresse adresseDB = testEntityManager.persist(adresse);
        assertThat(adresseDB).isNotNull();

        adresseRepository.delete(adresseDB);

        final Adresse adresseDB2 = testEntityManager.find(Adresse.class, adresse.getId());
        assertNull(adresseDB2);



    }
}
