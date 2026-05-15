package fr.eni.backend.dao;

import fr.eni.backend.bo.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
}
