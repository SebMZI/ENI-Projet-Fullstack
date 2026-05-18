package fr.eni.backend.dao;

import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.key.RolePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
