package fr.eni.backend.bo;

import fr.eni.backend.dao.PromotionRepository;
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
public class TestPromotionRepository {

    @Autowired
   TestEntityManager testEntityManager;

    @Autowired
    PromotionRepository promotionRepository;

    private List<CoursPlanifie> coursPlanifieList;
    private List<Eleve> eleveList;
    private Promotion promotion;

    @BeforeEach
    void jeuDeDonnees() {
        coursPlanifieList = new ArrayList<>();
        eleveList = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            final CoursPlanifie cours = CoursPlanifie
                    .builder()
                    .titre("Cours " + i)
                    .duree(2)
                    .dateDebut(LocalDate.now())
                    .dateFin(LocalDate.now())
                    .build();

            final Eleve eleve = Eleve
                    .builder()
                    .immatriculation("ENI_ELEVE_" + i)
                    .motDePasse("XXXXXXXXXX")
                    .nom("Eleve nom :" + i)
                    .prenom("Eleve prenom "+ i)
                    .email("mail" + i + "@campus-eni.fr")
                    .telephone("XXXXXXXXXX")
                    .dateCreation(LocalDate.now())
                    .emailPersonnel("mail@imap.fr")
                    .dateInscription(LocalDate.now())
                    .build();

            coursPlanifieList.add(cours);
            eleveList.add(eleve);
        }

        promotion = Promotion
                .builder()
                .nom("Promotion")
                .dateDebut(LocalDate.of(2026, 6, 15))
                .dateFin(LocalDate.of(2026, 12, 12))
                .coursPlanifies(coursPlanifieList)
                .eleves(eleveList)
                .build();
    }

    @Test
    public void testPromotion_save (){
        final Promotion promotionDB = promotionRepository.save(promotion);

        assertThat(promotionDB.getId()).isGreaterThan(0);

        assertThat(promotionDB.getCoursPlanifies()).isNotNull();
        assertThat(promotionDB.getCoursPlanifies()).isNotEmpty();

        assertThat(promotionDB.getEleves()).isNotNull();
        assertThat(promotionDB.getEleves()).isNotEmpty();

        assertThat(promotionDB.getCoursPlanifies().size()).isEqualTo(3);
        assertThat(promotionDB.getEleves().size()).isEqualTo(3);

        log.info("TestPromotion_save promotion {}", promotionDB.getEleves());
    }

    @Test
    public void testPromotion_delete(){
        final Promotion promotionDB = testEntityManager.persist(promotion);
        testEntityManager.flush();

        assertThat(promotionDB.getId()).isGreaterThan(0);
        assertThat(promotionDB.getCoursPlanifies()).isNotNull();
        assertThat(promotionDB.getCoursPlanifies()).isNotEmpty();
        assertThat(promotionDB.getEleves()).isNotNull();
        assertThat(promotionDB.getEleves()).isNotEmpty();
        assertThat(promotionDB.getCoursPlanifies().size()).isEqualTo(3);
        assertThat(promotionDB.getEleves().size()).isEqualTo(3);

        List<Eleve> listeElevesDB = promotionDB.getEleves();
        List<String> idsEleleveDB = listeElevesDB
                .stream()
                .map(Eleve::getImmatriculation)
                .collect(Collectors.toList());

        List<CoursPlanifie> listeCoursPlanifiesDB = promotionDB.getCoursPlanifies();
        List<Integer> idsCoursPlanifieDB = listeCoursPlanifiesDB
                .stream()
                .map(CoursPlanifie::getId)
                .collect(Collectors.toList());

        promotionRepository.delete(promotionDB);

        // Vérification que l'entité a été supprimée
        final Promotion promotionDB2 = testEntityManager.find(Promotion.class, promotion.getId());
        assertNull(promotionDB2);

        //Vérification que les cours planifiés sont supprimés
        idsCoursPlanifieDB.forEach(id -> {
            assertThat(id).isNotNull();
            final CoursPlanifie coursPlanifieDB2 = testEntityManager.find(CoursPlanifie.class, id);
            assertNull(coursPlanifieDB2);
            log.info(String.valueOf(coursPlanifieDB2));
        });

        //Vérification que les élèves sont conservés
        idsEleleveDB.forEach(id -> {
            assertThat(id).isNotNull();
            final Eleve eleveDB2 = testEntityManager.find(Eleve.class, id);
            assertThat(eleveDB2.getImmatriculation()).isNotNull();
            assertThat(eleveDB2).isNotNull();
            log.info(eleveDB2.toString());
        });

        log.info("Promotion: {}", promotionDB2);


    }



}
