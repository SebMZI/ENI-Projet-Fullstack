package fr.eni.backend.service;
import fr.eni.backend.dto.CoursDTO;
import fr.eni.backend.bo.Cursus;
import fr.eni.backend.bo.Filiere;
import fr.eni.backend.dao.CursusRepository;
import fr.eni.backend.dao.FiliereRepository;
import fr.eni.backend.dto.CursusDTO;
import fr.eni.backend.dto.CursusRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursusService {

    private final CursusRepository cursusRepository;
    private final FiliereRepository filiereRepository;

    private CursusDTO toDTO(Cursus cursus) {
        return CursusDTO.builder()
                .id(cursus.getId())
                .intitule(cursus.getIntitule())
                .idFiliere(cursus.getFieldId()) 
                .build();
    }

    public List<CursusDTO> findAll() {
        return cursusRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CursusDTO findById(Integer id) {
        Cursus cursus = cursusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cursus introuvable"));
        return toDTO(cursus);
    }

    public List<CoursDTO> findCoursByCursus(Integer cursusId) {
        Cursus cursus = cursusRepository.findById(cursusId)
                .orElseThrow(() -> new RuntimeException("Cursus introuvable"));
        return cursus.getCours().stream()
                .map(c -> CoursDTO.builder()
                        .id(c.getId())
                        .titre(c.getTitre())
                        .duree(c.getDuree())
                        .idCursus(cursusId)
                        .build())
                .toList();
    }

    public CursusDTO create(CursusRequestDTO request) {
        filiereRepository.findById(request.getIdFiliere())
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));

        Cursus cursus = Cursus.builder()
                .intitule(request.getIntitule())
                .fieldId(request.getIdFiliere())
                .build();
        Cursus saved = cursusRepository.save(cursus);
        return toDTO(saved);
    }

    public CursusDTO update(Integer id, CursusRequestDTO request) {
        Cursus existing = cursusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cursus introuvable"));

        filiereRepository.findById(request.getIdFiliere())
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));

        existing.setIntitule(request.getIntitule());
        existing.setFieldId(request.getIdFiliere());
        return toDTO(cursusRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!cursusRepository.existsById(id)) {
            throw new RuntimeException("Cursus introuvable avec l'id : " + id);
        }
        cursusRepository.deleteById(id);
    }
}