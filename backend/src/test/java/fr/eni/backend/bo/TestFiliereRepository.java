package fr.eni.backend.bo;

import fr.eni.backend.dao.FiliereRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestFiliereRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private FiliereRepository filiereRepository;

    private List<Cursus> dataCursus;
    private Filiere filiere;

    @BeforeEach
    void jeuDeDonnees() {
        dataCursus = new ArrayList<>();

        //String immatriculation = "ENI_CAMPUS_20231187";

        for (int i = 1; i < 4; i++) {
            final Cursus cursus = Cursus
                    .builder()
                    .intitule("pnom " + i)
                    .build();

            dataCursus.add(cursus);


        }

        filiere = Filiere
                .builder()
                .nom("Filiere 1")
                .cursus(dataCursus)
                .build();

        filiere.setCursus(dataCursus);
    }

    @Test
    public void TestFiliereRepository_save() {
        final Filiere filiereDB = filiereRepository.save(filiere);

        assertThat(filiereDB.getId()).isGreaterThan(0);

        // Vérification de la cascade de l'association
        assertThat(filiereDB.getCursus()).isNotNull();
        assertThat(filiereDB.getCursus()).isNotEmpty();
        assertThat(filiereDB.getCursus().size()).isEqualTo(3);
        log.info(filiereDB.toString());
    }


    @Test
    public void TestFiliereRepository_update() {
        final Filiere filiere = Filiere
                .builder()
                .nom("Filiere 1")
                .build();

        testEntityManager.persist(filiere);
        testEntityManager.flush();

        assertThat(filiere.getId()).isGreaterThan(0);
        log.info(filiere.toString());


        filiereRepository.delete(filiere);

        final Filiere filiereDB = testEntityManager.find(Filiere.class, filiere.getId());
        assertNull(filiereDB);
    }

    @Test
    public void TestFiliereRepository_delete() {

        final Filiere filiereDB = testEntityManager.persist(filiere);
        testEntityManager.flush();

        assertThat(filiereDB.getId()).isGreaterThan(0);
        assertThat(filiereDB.getCursus()).isNotNull();
        assertThat(filiereDB.getCursus()).isNotEmpty();

        List<Cursus> listeCursusDB = filiereDB.getCursus();
        List<Integer> idsCursusDB = listeCursusDB
                .stream()
                .map(Cursus::getId)
                .collect(Collectors.toList());

        log.info("idsCursusDB: " + idsCursusDB);

        filiereRepository.delete(filiereDB);

        // Vérification que l'entité a été supprimée
        final Filiere filiereDB2 = testEntityManager.find(Filiere.class, filiere.getId());
        assertNull(filiereDB2);

        // Vérifier que tous les Cursus sont supprimés par cascade
        assertThat(idsCursusDB).isNotNull();
        assertThat(idsCursusDB).isNotEmpty();

        idsCursusDB.forEach(id -> {
            assertThat(id).isNotNull();
            Cursus cursusDB = testEntityManager.find(Cursus.class, id);
            assertNull(cursusDB);
        });
    }

    @Test
    public void TestFiliereRepository_orphanRemoval(){
        final Filiere filiereDB = testEntityManager.persist(filiere);
        testEntityManager.flush();

        assertThat(filiereDB.getId()).isGreaterThan(0);
        assertThat(filiereDB.getCursus()).isNotNull();
        assertThat(filiereDB.getCursus()).isNotEmpty();

        List<Cursus> listeCursusDB = filiereDB.getCursus();
        List<Integer> idsCursusDB = listeCursusDB
                .stream()
                .map(Cursus::getId)
                .collect(Collectors.toList());

        //detachement cursus
        filiere.getCursus().clear();

        filiereRepository.delete(filiereDB);

        // Vérification que l'entité a été supprimée
        final Filiere filiereDB2 = testEntityManager.find(Filiere.class, filiere.getId());
        assertNull(filiereDB2);

        // Vérification que l'entité a été supprimée
        assertThat(idsCursusDB).isNotNull();
        assertThat(idsCursusDB).isNotEmpty();
        idsCursusDB.forEach(id -> {
            assertThat(id).isNotNull();
            Cursus cursusDB = testEntityManager.find(Cursus.class, id);
            assertNull(cursusDB);
        });
    }
}
