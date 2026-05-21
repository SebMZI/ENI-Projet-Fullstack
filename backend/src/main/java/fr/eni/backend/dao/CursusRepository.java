package fr.eni.backend.dao;

import fr.eni.backend.bo.Cursus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CursusRepository extends JpaRepository<Cursus, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Cursus c SET c.fieldId = :filiereId WHERE c.id = :id")
    void setFiliereId(Integer id, Integer filiereId);
}