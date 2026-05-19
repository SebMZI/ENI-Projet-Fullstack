package fr.eni.backend.bo;

import fr.eni.backend.dao.CoursRepositiry;
import fr.eni.backend.dao.FormateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestCoursRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CoursRepositiry coursRepositiry;

    @Test
    public void testCours_save() {
        final Cours cours = Cours
                .builder()
                .titre("Javascript")
                .duree(2)
                .build();

        final Cours coursDB = coursRepositiry.save(cours);
        assertThat(coursDB.getId()).isGreaterThan(0);
    }

    @Test
    public void testCours_delete() {
        final Cours cours = Cours
                .builder()
                .titre("Javascript")
                .duree(2)
                .build();

        final Cours coursDB = testEntityManager.persist(cours);
        testEntityManager.flush();

        coursRepositiry.delete(cours);

        final Cours coursDB2 = testEntityManager.find(CoursPlanifie.class, cours.getId());
        assertNull(coursDB2);
    }

}
