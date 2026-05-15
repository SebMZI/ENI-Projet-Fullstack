package fr.eni.backend.dao;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.bo.CoursPlanifie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursPlanifiesRepository extends JpaRepository<CoursPlanifie, Integer> {
}
