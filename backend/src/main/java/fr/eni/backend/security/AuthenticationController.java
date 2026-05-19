package fr.eni.backend.security;


import fr.eni.backend.dto.UtilisateurDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/auth")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest request, HttpServletResponse response){
        try {
            AuthenticationResponse auth = authenticationService.authenticate(request);

            ResponseCookie cookie = ResponseCookie.from("jwt_token_app_erp", auth.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(60 * 60)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return ResponseEntity.ok(auth.getUtilisateurDto());
        }catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<?> checkLoggedIn(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(authenticationService.checkLoggedIn(request));
        }catch (Exception e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }
}
