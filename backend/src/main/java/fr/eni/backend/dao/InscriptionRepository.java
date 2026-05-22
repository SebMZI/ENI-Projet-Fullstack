package fr.eni.backend.dao;

import fr.eni.backend.bo.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    boolean existsByEleveImmatriculationAndCoursPlanifieId(String immatriculation, Integer coursPlanifieId);
    boolean existsByEleveImmatriculationAndCoursPlanifieTitre(String immatriculation, String titre);
    List<Inscription> findByEleveImmatriculation(String immatriculation);
}