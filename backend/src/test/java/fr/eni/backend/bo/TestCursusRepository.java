package fr.eni.backend.bo;

import fr.eni.backend.dao.CursusRepository;
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
public class TestCursusRepository {

    @Autowired
    CursusRepository cursusRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private List<Cours> coursList;
    private List<Promotion> promotionList;
    private Cursus cursus;

    @BeforeEach
    void jeuDeDonnees() {
        coursList = new ArrayList<>();
        promotionList = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            final Cours cours = Cours
                    .builder()
                    .titre("Cours " + i)
                    .duree(2)
                    .build();

            final Promotion promotion = Promotion
                    .builder()
                    .nom("Promotion :" + i)
                    .dateDebut(LocalDate.of(2025, 5, i))
                    .dateFin(LocalDate.of(2025, 10, i + 14))
                    .build();

            coursList.add(cours);
            promotionList.add(promotion);
        }

        cursus = Cursus
                .builder()
                .intitule("Mon cursus")
                .cours(coursList)
                .promotions(promotionList)
                .build();

        cursus.setCours(coursList);
        cursus.setPromotions(promotionList);
    }

    @Test
    public void testCursus_save(){
        final Cursus cursusDB = cursusRepository.save(cursus);

        assertThat(cursusDB.getId()).isGreaterThan(0);

        //Verification cours et promotions
        assertThat(cursusDB.getCours()).isNotNull();
        assertThat(cursusDB.getPromotions()).isNotNull();

        assertThat(cursusDB.getCours()).isNotEmpty();
        assertThat(cursusDB.getPromotions()).isNotEmpty();

        assertThat(cursusDB.getCours().size()).isEqualTo(3);
        assertThat(cursusDB.getPromotions().size()).isEqualTo(3);

        log.info("Cursus: {}", cursusDB);
    }

    //Pas cascade avec promotion et cours
    @Test
    public void testCursus_delete() {
        final Cursus cursusDB = testEntityManager.persist(cursus);
        testEntityManager.flush();

        assertThat(cursusDB.getId()).isGreaterThan(0);

        List<Cours> listeCoursDB = cursusDB.getCours();
        List<Integer> idsCoursDB = listeCoursDB
                .stream()
                .map(Cours::getId)
                .collect(Collectors.toList());

        List<Promotion> listePromotionDB = cursusDB.getPromotions();
        List<Integer> idsPromotionDB = listePromotionDB
                .stream()
                .map(Promotion::getId)
                .collect(Collectors.toList());

        cursusRepository.delete(cursusDB);

        // Vérification que l'entité a été supprimée
        final Cursus cursusDB2 = testEntityManager.find(Cursus.class, cursus.getId());
        assertNull(cursusDB2);


        log.info(idsCoursDB.toString());
        log.info(idsPromotionDB.toString());

        //Vérification que les cours et les promotions sont conservées
        idsCoursDB.forEach(id -> {
            assertThat(id).isNotNull();
            final Cours coursDB2 = testEntityManager.find(Cours.class, id);
            assertThat(coursDB2.getId()).isGreaterThan(0);
            assertThat(coursDB2).isNotNull();
        });

        idsPromotionDB.forEach(id -> {
            assertThat(id).isNotNull();
            final Promotion promotionDB2 = testEntityManager.find(Promotion.class, id);
            assertThat(promotionDB2.getId()).isGreaterThan(0);
            assertThat(promotionDB2).isNotNull();
        });

        log.info("Cursus: {}", cursusDB2);
    }



}
