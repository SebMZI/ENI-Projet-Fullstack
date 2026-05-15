package fr.eni.backend.controller;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.service.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")   //  a adapter plus tard
public class CoursController {

    private final CoursService coursService;

    @GetMapping
    public ResponseEntity<List<Cours>> getAll() {
        return ResponseEntity.ok(coursService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cours> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(coursService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Cours> create(@RequestBody Cours cours) {
        return ResponseEntity.ok(coursService.create(cours));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cours> update(@PathVariable Integer id,
                                        @RequestBody Cours cours) {
        return ResponseEntity.ok(coursService.update(id, cours));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        coursService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}