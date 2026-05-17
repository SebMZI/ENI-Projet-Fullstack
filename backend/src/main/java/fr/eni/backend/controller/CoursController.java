package fr.eni.backend.controller;

import fr.eni.backend.dto.CoursDTO;
import fr.eni.backend.dto.CoursRequestDTO;
import fr.eni.backend.service.CoursService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CoursController {

    private final CoursService coursService;

    @GetMapping
    public ResponseEntity<List<CoursDTO>> getAll() {
        return ResponseEntity.ok(coursService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoursDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(coursService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CoursDTO> create(@Valid @RequestBody CoursRequestDTO request) {
        return ResponseEntity.ok(coursService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoursDTO> update(@PathVariable Integer id,
                                           @Valid @RequestBody CoursRequestDTO request) {
        return ResponseEntity.ok(coursService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        coursService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}