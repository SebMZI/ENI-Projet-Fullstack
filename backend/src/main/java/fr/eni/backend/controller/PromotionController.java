package fr.eni.backend.controller;

import fr.eni.backend.bo.Eleve; 
import fr.eni.backend.bo.Promotion;  
import fr.eni.backend.dto.EleveDTO;
import fr.eni.backend.dto.PromotionDTO;
import fr.eni.backend.dto.PromotionRequestDTO;
import fr.eni.backend.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAll() {
        return ResponseEntity.ok(promotionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.findById(id));
    }

    @GetMapping("/formateurs/{immatriculation}")
    public ResponseEntity<List<PromotionDTO>> getByFormateur(@PathVariable String immatriculation) {
        return ResponseEntity.ok(promotionService.findByFormateur(immatriculation));
    }

    @PostMapping
    public ResponseEntity<PromotionDTO> create(@Valid @RequestBody PromotionRequestDTO request) {
        return ResponseEntity.ok(promotionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionDTO> update(@PathVariable Integer id,
                                               @Valid @RequestBody PromotionRequestDTO request) {
        return ResponseEntity.ok(promotionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        promotionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // ========== Inscription des eleves ==========

    @PostMapping("/{promotionId}/eleves/{eleveId}")
    public ResponseEntity<PromotionDTO> inscrireEleve(
            @PathVariable Integer promotionId,
            @PathVariable String eleveId) { 
        Promotion updated = promotionService.inscrireEleve(promotionId, eleveId);
        return ResponseEntity.ok(promotionService.convertToDTO(updated));
    }

    @DeleteMapping("/{promotionId}/eleves/{eleveId}")
    public ResponseEntity<PromotionDTO> desinscrireEleve(
            @PathVariable Integer promotionId,
            @PathVariable String eleveId) {  
        Promotion updated = promotionService.desinscrireEleve(promotionId, eleveId);
        return ResponseEntity.ok(promotionService.convertToDTO(updated));
    }

    @GetMapping("/{promotionId}/eleves")
    public ResponseEntity<List<EleveDTO>> getEleves(@PathVariable Integer promotionId) {
        return ResponseEntity.ok(promotionService.getElevesByPromotion(promotionId));
    }
}