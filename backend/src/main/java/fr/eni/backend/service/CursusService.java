package fr.eni.backend.service;

import fr.eni.backend.bo.Cursus;
import fr.eni.backend.dao.CursusRepository;
import fr.eni.backend.dto.CursusDTO;
import fr.eni.backend.dto.CursusRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursusService {

    private final CursusRepository cursusRepository;

    private CursusDTO toDTO(Cursus cursus) {
        return CursusDTO.builder()
                .id(cursus.getId())
                .intitule(cursus.getIntitule())
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

    public CursusDTO create(CursusRequestDTO request) {
        Cursus cursus = Cursus.builder()
                .intitule(request.getIntitule())
                .build();
        return toDTO(cursusRepository.save(cursus));
    }

    public CursusDTO update(Integer id, CursusRequestDTO request) {
        Cursus existing = cursusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cursus introuvable"));
        existing.setIntitule(request.getIntitule());
        return toDTO(cursusRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!cursusRepository.existsById(id)) {
            throw new RuntimeException("Cursus introuvable avec l'id : " + id);
        }
        cursusRepository.deleteById(id);
    }
}