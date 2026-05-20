package fr.eni.backend.dao;

import fr.eni.backend.bo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
