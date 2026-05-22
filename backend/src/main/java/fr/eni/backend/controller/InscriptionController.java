package fr.eni.backend.controller;

import fr.eni.backend.service.InscriptionService;
import fr.eni.backend.dto.InscriptionDTO;
import fr.eni.backend.dto.InscriptionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    // Inscription normale (vérifie ordre et doublon)
    @PostMapping("/unite")
    public ResponseEntity<InscriptionDTO> inscrireUnite(@RequestBody InscriptionRequestDTO request) {
        return ResponseEntity.ok(inscriptionService.inscrireUnite(request, false));
    }

    @GetMapping("/eleve/{immatriculation}")
    public ResponseEntity<List<InscriptionDTO>> getByEleve(@PathVariable String immatriculation) {
        return ResponseEntity.ok(inscriptionService.findByEleve(immatriculation));
    }
    // Inscription forcée (ignore l'ordre)
    @PostMapping("/unite/forcer")
    public ResponseEntity<InscriptionDTO> inscrireUniteForcee(@RequestBody InscriptionRequestDTO request) {
        return ResponseEntity.ok(inscriptionService.inscrireUnite(request, true));
    }

}