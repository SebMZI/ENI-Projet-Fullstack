package fr.eni.backend.dao;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.bo.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EleveRepository extends JpaRepository<Eleve, String> {

    @Query("SELECT e FROM Eleve e JOIN Promotion p WHERE p.id = :id")
    List<Eleve> findElevesByPromotion(@Param("id") Integer id);
}
