package fr.eni.backend.service;

import fr.eni.backend.bo.Cours;
import fr.eni.backend.bo.CoursPlanifie;
import fr.eni.backend.bo.Formateur;
import fr.eni.backend.bo.Promotion;
import fr.eni.backend.dao.CoursPlanifieRepository; 
import fr.eni.backend.dao.CoursRepository;
import fr.eni.backend.dao.FormateurRepository;
import fr.eni.backend.dao.PromotionRepository;
import fr.eni.backend.dto.CoursPlanifieDTO;
import fr.eni.backend.dto.CoursPlanifieRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursPlanifieService {

    private final CoursPlanifieRepository coursPlanifieRepository; 
    private final CoursRepository coursRepository;
    private final PromotionRepository promotionRepository;
    private final FormateurRepository formateurRepository;

    private CoursPlanifieDTO toDTO(CoursPlanifie cp) {
        return CoursPlanifieDTO.builder()
                .id(cp.getId())
                .titre(cp.getTitre())
                .duree(cp.getDuree())
                .dateDebut(cp.getDateDebut())
                .dateFin(cp.getDateFin())
                .idFormateur(cp.getFormateur() != null ? cp.getFormateur().getImmatriculation() : null)  // ← changé
                .nomFormateur(cp.getFormateur() != null ? cp.getFormateur().getNom() + " " + cp.getFormateur().getPrenom() : null)
                .idPromotion(cp.getPromotion() != null ? cp.getPromotion().getId() : null)
                .build();
    }

    public List<CoursPlanifieDTO> findAll() {
        return coursPlanifieRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public CoursPlanifieDTO findById(Integer id) {
        CoursPlanifie cp = coursPlanifieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours planifié introuvable"));
        return toDTO(cp);
    }

    public List<CoursPlanifieDTO> findByPromotion(Integer promotionId) {
        return coursPlanifieRepository.findByPromotionId(promotionId).stream()
                .map(this::toDTO)
                .toList();
    }

    public CoursPlanifieDTO create(Integer promotionId, CoursPlanifieRequestDTO request) {
        // Vérifie que la promotion existe
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));

        // Vérifie que le cours existe
        Cours cours = coursRepository.findById(request.getIdCours())
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));

        // Vérifie les dates
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        // Vérifie le formateur (optionnel)
        Formateur formateur = null;
        if (request.getIdFormateur() != null) {
            formateur = formateurRepository.findById(request.getIdFormateur())
                    .orElseThrow(() -> new RuntimeException("Formateur introuvable"));
        }

        // Crée le cours planifié
        CoursPlanifie cp = CoursPlanifie.builder()
                .titre(cours.getTitre())
                .duree(cours.getDuree())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .formateur(formateur)
                .promotion(promotion)  // ← Lien avec la promotion
                .build();

        CoursPlanifie saved = coursPlanifieRepository.save(cp);

        //  CES DEUX LIGNES SONT INUTILES — JPA gère déjà la relation via PROMOTION_ID
        promotion.getCoursPlanifies().add(saved);
        promotionRepository.save(promotion);

        return toDTO(saved);
    }


    public CoursPlanifieDTO update(Integer id, CoursPlanifieRequestDTO request) {
        
        CoursPlanifie cp = coursPlanifieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours planifié introuvable"));

        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        cp.setDateDebut(request.getDateDebut());
        cp.setDateFin(request.getDateFin());

        if (request.getIdFormateur() != null) {
            Formateur formateur = formateurRepository.findById(request.getIdFormateur())
                    .orElseThrow(() -> new RuntimeException("Formateur introuvable"));
            cp.setFormateur(formateur);
        } else {
            cp.setFormateur(null);
        }

        return toDTO(coursPlanifieRepository.save(cp));
    }

    public void deleteById(Integer id) {
        if (!coursPlanifieRepository.existsById(id)) {
            throw new RuntimeException("Cours planifié introuvable");
        }
        coursPlanifieRepository.deleteById(id);
    }
}