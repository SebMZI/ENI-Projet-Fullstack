package fr.eni.backend;

import fr.eni.backend.bo.Role;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.RoleRepository;
import fr.eni.backend.dao.UtilisateurRepository;
import fr.eni.backend.security.AuthenticationRequest;
import fr.eni.backend.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@AllArgsConstructor
@SpringBootTest
public class TestAuth {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationService aService;

    @Autowired
    private PasswordEncoder pEncoder;

    @BeforeEach
    void createUser() {
        String mdpEncoded = pEncoder.encode("JeSuisAnneLise");
        Utilisateur utilisateur = Utilisateur.builder().immatriculation("ENI_25039285").nom("BAILLE").prenom("Anne-Lise").motDePasse(mdpEncoded).email("abaille@campus-eni.fr").telephone("0600000000").build();
        Role roleAdmin = Role.builder().immatriculation("ENI_25039285").role("ROLE_ADMIN").build();

        roleRepository.save(roleAdmin);
        roleRepository.flush();

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(roleAdmin);
        utilisateur.setRoles(roles);
        utilisateurRepository.save(utilisateur);

    }

    @Test
    void test_auth_correct_credentials() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPseudo("abaille@campus-eni.fr");
        request.setPassword("JeSuisAnneLise");
        System.out.println(request);
        System.out.println(aService.authenticate(request));
    }
}
