package fr.eni.backend.controller;

import fr.eni.backend.dto.CoursPlanifieDTO;
import fr.eni.backend.dto.CoursPlanifieRequestDTO;
import fr.eni.backend.service.CoursPlanifieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cours-planifies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CoursPlanifieController {

    private final CoursPlanifieService coursPlanifieService;

    @GetMapping
    public ResponseEntity<List<CoursPlanifieDTO>> getAll() {
        return ResponseEntity.ok(coursPlanifieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoursPlanifieDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(coursPlanifieService.findById(id));
    }

    @GetMapping("/promotion/{promotionId}")
    public ResponseEntity<List<CoursPlanifieDTO>> getByPromotion(@PathVariable Integer promotionId) {
        return ResponseEntity.ok(coursPlanifieService.findByPromotion(promotionId));
    }

    @PostMapping("/promotion/{promotionId}")
    public ResponseEntity<CoursPlanifieDTO> create(
            @PathVariable Integer promotionId,
            @Valid @RequestBody CoursPlanifieRequestDTO request) {
        return ResponseEntity.ok(coursPlanifieService.create(promotionId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        coursPlanifieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}