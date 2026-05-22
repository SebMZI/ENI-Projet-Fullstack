package fr.eni.backend.dao;

import fr.eni.backend.bo.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query("SELECT p FROM Promotion p JOIN p.eleves e WHERE e.immatriculation = :immatriculation")
    List<Promotion> findByElevesImmatriculation(@Param("immatriculation") String immatriculation);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO STUDENT (USER_REGISTRATION) VALUES (:immatriculation)", nativeQuery = true)
    void insererEleveDansStudent(@Param("immatriculation") String immatriculation);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO PROMOTION_STUDENT (PROMOTION_ID, STUDENT_ID) VALUES (:promotionId, :studentId)", nativeQuery = true)
    void insererDansPromotionStudent(@Param("promotionId") Integer promotionId, @Param("studentId") String studentId);
}
