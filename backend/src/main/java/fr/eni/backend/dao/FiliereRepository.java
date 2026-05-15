package fr.eni.backend.dao;

import fr.eni.backend.bo.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiliereRepository extends JpaRepository<Filiere, Integer> {
}
