package fr.eni.backend.dao;

import fr.eni.backend.bo.CoursPlanifie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoursPlanifieRepository extends JpaRepository<CoursPlanifie, Integer> {
    List<CoursPlanifie> findByPromotionId(Integer promotionId);
}