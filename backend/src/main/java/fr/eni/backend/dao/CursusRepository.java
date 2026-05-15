package fr.eni.backend.dao;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.bo.Cursus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursusRepository extends JpaRepository<Cursus, Integer> {
}
