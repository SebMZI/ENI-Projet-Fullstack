package fr.eni.backend.service;

import fr.eni.backend.bo.Filiere;
import fr.eni.backend.repository.FiliereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FiliereService {

    private final FiliereRepository filiereRepository;

    public List<Filiere> findAll() {
        return filiereRepository.findAll();
    }

    public Filiere findById(Integer id) {
        return filiereRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Filière introuvable"));
    }

    public Filiere create(Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    public Filiere update(Integer id, Filiere filiere) {
        Filiere existing = findById(id);
        existing.setNom(filiere.getNom());
        return filiereRepository.save(existing);
    }


    public void deleteById(Integer id) {
        if (!filiereRepository.existsById(id)) {
            throw new RuntimeException("Filière introuvable avec l'id : " + id);
        }
        filiereRepository.deleteById(id);
    }
}