package fr.eni.backend.controller;

import fr.eni.backend.bo.Filiere;
import fr.eni.backend.service.FiliereService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/filieres")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")   //  a adapter plus tard
public class FiliereController {

    private final FiliereService filiereService;

    @GetMapping
    public ResponseEntity<List<Filiere>> getAll() {
        return ResponseEntity.ok(filiereService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filiere> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(filiereService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Filiere> create(@RequestBody Filiere filiere) {
        return ResponseEntity.ok(filiereService.create(filiere));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filiere> update(@PathVariable Integer id,
                                          @RequestBody Filiere filiere) {
        return ResponseEntity.ok(filiereService.update(id, filiere));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        filiereService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}