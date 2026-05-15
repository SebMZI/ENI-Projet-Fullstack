package fr.eni.backend.dao;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.bo.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EleveRepository extends JpaRepository<Eleve, Integer> {
}
