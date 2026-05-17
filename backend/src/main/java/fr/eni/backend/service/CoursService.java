package fr.eni.backend.service;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.dao.CoursRepositiry;
import fr.eni.backend.dto.CoursDTO;
import fr.eni.backend.dto.CoursRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursService {

    private final CoursRepositiry coursRepository; 

    private CoursDTO toDTO(Cours cours) {
        return CoursDTO.builder()
                .id(cours.getId())
                .titre(cours.getTitre())
                .duree(cours.getDuree())
                .build();
    }

    public List<CoursDTO> findAll() {
        return coursRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CoursDTO findById(Integer id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));
        return toDTO(cours);
    }

    public CoursDTO create(CoursRequestDTO request) {
        Cours cours = Cours.builder()
                .titre(request.getTitre())
                .duree(request.getDuree())
                .build();
        return toDTO(coursRepository.save(cours));
    }

    public CoursDTO update(Integer id, CoursRequestDTO request) {
        Cours existing = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));
        existing.setTitre(request.getTitre());
        existing.setDuree(request.getDuree());
        return toDTO(coursRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!coursRepository.existsById(id)) {
            throw new RuntimeException("Cours introuvable avec l'id : " + id);
        }
        coursRepository.deleteById(id);
    }
}