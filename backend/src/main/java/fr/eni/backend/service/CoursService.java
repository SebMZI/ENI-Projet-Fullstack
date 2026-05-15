package fr.eni.backend.service;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.repository.CoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursService {

    private final CoursRepository coursRepository;

    public List<Cours> findAll() {
        return coursRepository.findAll();
    }

    public Cours findById(Integer id) {
        return coursRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cours introuvable"));
    }

    public Cours create(Cours cours) {
        return coursRepository.save(cours);
    }

    public Cours update(Integer id, Cours cours) {
        Cours existing = findById(id);
        existing.setIntitule(cours.getIntitule());
        existing.setReference(cours.getReference());
        existing.setDuree(cours.getDuree());
        return coursRepository.save(existing);
    }


    public void deleteById(Integer id) {
        if (!coursRepository.existsById(id)) {
            throw new RuntimeException("Cours introuvable avec l'id : " + id);
        }
        coursRepository.deleteById(id);
    }
}