package fr.eni.backend.controller;
import fr.eni.backend.dto.CoursDTO;
import fr.eni.backend.dto.CursusDTO;
import fr.eni.backend.dto.CursusRequestDTO;
import fr.eni.backend.service.CursusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursus")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CursusController {

    private final CursusService cursusService;

    @GetMapping
    public ResponseEntity<List<CursusDTO>> getAll() {
        return ResponseEntity.ok(cursusService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursusDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(cursusService.findById(id));
    }

    @GetMapping("/{id}/cours")
    public ResponseEntity<List<CoursDTO>> getCoursByCursus(@PathVariable Integer id) {
        return ResponseEntity.ok(cursusService.findCoursByCursus(id));
    }

    @PostMapping
    public ResponseEntity<CursusDTO> create(@Valid @RequestBody CursusRequestDTO request) {
        return ResponseEntity.ok(cursusService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursusDTO> update(@PathVariable Integer id,
                                            @Valid @RequestBody CursusRequestDTO request) {
        return ResponseEntity.ok(cursusService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cursusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}