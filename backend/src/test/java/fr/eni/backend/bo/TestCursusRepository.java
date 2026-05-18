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

import static org.assertj.core.api.Assertions.assertThat;

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
    public void TestCursus_save(){
        Cursus cursusDB = cursusRepository.save(cursus);

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
}
