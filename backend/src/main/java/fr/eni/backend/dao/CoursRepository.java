package fr.eni.backend.dao;

import fr.eni.backend.bo.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoursRepository extends JpaRepository<Cours, Integer> {

    @Query("SELECT c FROM Cours c WHERE TYPE(c) = Cours")
    List<Cours> findAllCatalogueOnly();
}