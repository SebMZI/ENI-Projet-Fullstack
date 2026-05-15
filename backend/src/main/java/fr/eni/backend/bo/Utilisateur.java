package fr.eni.backend.bo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "USERS")
public class Utilisateur implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_REGISTRATION", nullable = false, unique = true)
    private String immatriculation;

    @Column(name = "USER_LAST_NAME", nullable = false, length = 100)
    private String nom;

    @Column(name = "USER_FIRST_NAME", nullable = false, length = 100)
    private String prenom;

    @Column(name = "USER_PASSWORD", nullable = false, length = 255)
    private String motDePasse;

    @Column(name = "USER_EMAIL", nullable = false, length = 150)
    private String emailEni;

    @Column(name = "USER_PHONE", nullable = false, length = 150)
    private String telephone;

    @Column(name = "CREATION_DATE")
    private LocalDate dateCreation;

    // TODO A modifier quand les relations seront faites
    private String authority;

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return emailEni;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authority));
    }
}
