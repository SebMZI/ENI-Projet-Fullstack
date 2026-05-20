package fr.eni.backend.bo;

import fr.eni.backend.dao.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@DataJpaTest
public class TestRoleRepository {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @Test
    public void testRole_save() {
        role = Role
                .builder()
                .role("ROLE_ADMIN")
                .build();

        final Role roleDB = roleRepository.save(role);
        assertThat(roleDB).isNotNull();
    }

    @Test
    public void testRole_delete() {
        role = Role
                .builder()
                .role("ROLE_ADMIN")
                .build();

        final Role roleDB = testEntityManager.persist(role);
        assertThat(roleDB).isNotNull();

        roleRepository.delete(roleDB);

        final Role roleDB2 = testEntityManager.find(Role.class, role.getId());
        assertNull(roleDB2);
    }
}
