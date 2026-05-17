package fr.eni.backend.controller;

import fr.eni.backend.dto.FiliereDTO;
import fr.eni.backend.dto.FiliereRequestDTO;
import fr.eni.backend.service.FiliereService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/filieres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FiliereController {

    private final FiliereService filiereService;

    @GetMapping
    public ResponseEntity<List<FiliereDTO>> getAll() {
        return ResponseEntity.ok(filiereService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiliereDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(filiereService.findById(id));
    }

    @PostMapping
    public ResponseEntity<FiliereDTO> create(@Valid @RequestBody FiliereRequestDTO request) {
        return ResponseEntity.ok(filiereService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FiliereDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody FiliereRequestDTO request) {
        return ResponseEntity.ok(filiereService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        filiereService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}