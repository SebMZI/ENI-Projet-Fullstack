package fr.eni.backend.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "DISCR")
//@DiscriminatorValue(value = "U")
@Table(name = "USERS") // USER est un mot utilisé par SQL
public class Utilisateur implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @NonNull
    @NotBlank
    @Size(max = 50)
    @Column(name = "USER_REGISTRATION", nullable = false, unique = true)
    private String immatriculation;

    @NotNull
    @NotBlank
    @Size(max = 150)
    @Column(name = "USER_LAST_NAME", nullable = false, length = 100)
    private String nom;

    @NotNull
    @NotBlank
    @Size(max = 150)
    @Column(name = "USER_FIRST_NAME", nullable = false, length = 100)
    private String prenom;

    @NotNull
    @NotBlank
    @Size(max = 255)
    @Column(name = "USER_PASSWORD", nullable = false, length = 255)
    private String motDePasse;

    @NotBlank
    @Email
    @Column(name = "USER_EMAIL", nullable = false, unique = true, length = 150)
    @Pattern(regexp="^[\\w-\\.]+@campus-eni.fr$")
    private String email;

    @Size(max = 10)
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
        return email;
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
        return roles.stream().map(role ->  new SimpleGrantedAuthority(role.getRole())).toList();
    }

    @OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID")
    private Adresse adresse;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_REGISTRATION", referencedColumnName = "USER_REGISTRATION"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"))
    @ToString.Exclude
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

}
 