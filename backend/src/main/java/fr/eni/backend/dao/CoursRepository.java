package fr.eni.backend.dao;

import fr.eni.backend.bo.Cours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursRepository extends JpaRepository<Cours, Integer> {
}
