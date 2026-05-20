package fr.eni.backend.dao;

import fr.eni.backend.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    Utilisateur findUtilisateurByEmail(String username);
    boolean existsByEmail(String email);
    boolean existsByImmatriculation(String immatriculation);
    Optional<Utilisateur> findByEmail(String email);
}