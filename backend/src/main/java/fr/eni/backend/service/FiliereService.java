package fr.eni.backend.service;

import fr.eni.backend.bo.Filiere;
import fr.eni.backend.dao.FiliereRepository;
import fr.eni.backend.dto.FiliereDTO;
import fr.eni.backend.dto.FiliereRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FiliereService {

    private final FiliereRepository filiereRepository;

    // BO → DTO
    private FiliereDTO toDTO(Filiere filiere) {
        return FiliereDTO.builder()
                .id(filiere.getId())
                .nom(filiere.getNom())
                .build();
    }

    public List<FiliereDTO> findAll() {
        return filiereRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public FiliereDTO findById(Integer id) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));
        return toDTO(filiere);
    }

    public FiliereDTO create(FiliereRequestDTO request) {
        Filiere filiere = Filiere.builder()
                .nom(request.getNom())
                .build();
        return toDTO(filiereRepository.save(filiere));
    }

    public FiliereDTO update(Integer id, FiliereRequestDTO request) {
        Filiere existing = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));
        existing.setNom(request.getNom());
        return toDTO(filiereRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!filiereRepository.existsById(id)) {
            throw new RuntimeException("Filière introuvable avec l'id : " + id);
        }
        filiereRepository.deleteById(id);
    }
}