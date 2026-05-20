package fr.eni.backend.controller;

import fr.eni.backend.dto.UtilisateurDTO;
import fr.eni.backend.dto.UtilisateurRequestDTO;
import fr.eni.backend.service.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    // GET /api/utilisateurs
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAll() {
        return ResponseEntity.ok(utilisateurService.findAll());
    }

    // GET /api/utilisateurs/{immatriculation}
    @GetMapping("/{immatriculation}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable String immatriculation) {
        return ResponseEntity.ok(utilisateurService.findById(immatriculation));
    }

    // POST /api/utilisateurs
    @PostMapping
    public ResponseEntity<UtilisateurDTO> create(@Valid @RequestBody UtilisateurRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.create(request));
    }

    // PUT /api/utilisateurs/{immatriculation}
    @PutMapping("/{immatriculation}")
    public ResponseEntity<UtilisateurDTO> update(@PathVariable String immatriculation,
                                                 @Valid @RequestBody UtilisateurRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.update(immatriculation, request));
    }

    // DELETE /api/utilisateurs/{immatriculation}
    @DeleteMapping("/{immatriculation}")
    public ResponseEntity<Void> delete(@PathVariable String immatriculation) {
        utilisateurService.deleteById(immatriculation);
        return ResponseEntity.noContent().build();
    }
}