package fr.eni.backend.dao;

import fr.eni.backend.bo.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormateurRepository extends JpaRepository<Formateur, Integer> {
}
