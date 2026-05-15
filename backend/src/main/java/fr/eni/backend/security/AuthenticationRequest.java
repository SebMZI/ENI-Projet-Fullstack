package fr.eni.backend.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "pseudo")
public class AuthenticationRequest {
    private String pseudo;
    private String password;
}
