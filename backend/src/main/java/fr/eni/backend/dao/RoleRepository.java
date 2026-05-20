package fr.eni.backend.dao;

import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.key.RolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RolePK> {
    
    void deleteByImmatriculation(String immatriculation);
}