package fr.eni.backend.dao;

import fr.eni.backend.bo.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    //récupération des promotions d'un formateur
    @Query("SELECT p FROM Promotion p JOIN p.coursPlanifies c WHERE c.formateur.immatriculation = :immatriculation")
    List<Promotion> findPromotionsByFormateur(@Param("immatriculation") String immatriculation);
}
