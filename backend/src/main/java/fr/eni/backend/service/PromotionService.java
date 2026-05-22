package fr.eni.backend.service;

import fr.eni.backend.bo.Cursus;
import fr.eni.backend.bo.Eleve;
import fr.eni.backend.bo.Promotion;
import fr.eni.backend.bo.Utilisateur;
import fr.eni.backend.dao.UtilisateurRepository;
import fr.eni.backend.dao.CursusRepository;
import fr.eni.backend.dao.EleveRepository;
import fr.eni.backend.dao.PromotionRepository;
import fr.eni.backend.dto.PromotionDTO;
import fr.eni.backend.dto.PromotionRequestDTO;
import fr.eni.backend.dto.EleveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final CursusRepository cursusRepository;
    private final EleveRepository eleveRepository; 
    private final UtilisateurRepository utilisateurRepository;

    // Conversion Entité → DTO
    private PromotionDTO toDTO(Promotion promotion) {
        return PromotionDTO.builder()
                .id(promotion.getId())
                .nom(promotion.getNom())
                .dateDebut(promotion.getDateDebut())
                .dateFin(promotion.getDateFin())
                .idCursus(promotion.getCursus() != null ? promotion.getCursus().getId() : null)
                .build();
    }

   // Méthode publique pour le contrôleur
    public PromotionDTO convertToDTO(Promotion promotion) {
        return toDTO(promotion);
    }
    // CRUD de base

    public List<PromotionDTO> findAll() {
        return promotionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public PromotionDTO findById(Integer id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));
        return toDTO(promotion);
    }

    public List<PromotionDTO> findPromotionsByEleve(String immatriculation) {
        return promotionRepository.findByElevesImmatriculation(immatriculation)
                .stream().map(this::toDTO).toList();
    }

    public PromotionDTO create(PromotionRequestDTO request) {
    
        if (request.getDateDebut().isBefore(LocalDate.now())) {
            throw new RuntimeException("La date de début doit être dans le futur");
        }
    
        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        // Vérifie que le cursus existe
        Cursus cursus = cursusRepository.findById(request.getIdCursus())
                .orElseThrow(() -> new RuntimeException("Cursus introuvable"));

        Promotion promotion = Promotion.builder()
                .nom(request.getNom())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .cursus(cursus)
                .build();

        return toDTO(promotionRepository.save(promotion));
    }

    public PromotionDTO update(Integer id, PromotionRequestDTO request) {
        Promotion existing = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));

        if (request.getDateFin().isBefore(request.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        Cursus cursus = cursusRepository.findById(request.getIdCursus())
                .orElseThrow(() -> new RuntimeException("Cursus introuvable"));

        existing.setNom(request.getNom());
        existing.setDateDebut(request.getDateDebut());
        existing.setDateFin(request.getDateFin());
        existing.setCursus(cursus);

        return toDTO(promotionRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!promotionRepository.existsById(id)) {
            throw new RuntimeException("Promotion introuvable avec l'id : " + id);
        }
        promotionRepository.deleteById(id);
    }



    // nscription d'un élève à une promotion

    public Promotion inscrireEleve(Integer promotionId, String eleveImmatriculation) {

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));

        // Vérifier que l'utilisateur existe et a le rôle ELEVE
        Utilisateur utilisateur = utilisateurRepository.findById(eleveImmatriculation)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        boolean isEleve = utilisateur.getRoles().stream().anyMatch(r -> r.getRole().equals("ELEVE"));

        if (!isEleve) {
            throw new RuntimeException("L'utilisateur n'a pas le rôle ELEVE");
        }

        // Vérifier s'il est déjà inscrit
        List<Promotion> promos = promotionRepository.findByElevesImmatriculation(eleveImmatriculation);
        boolean dejaInscrit = promos.stream().anyMatch(p -> p.getId().equals(promotionId));
        if (dejaInscrit) {
            throw new RuntimeException("Élève déjà inscrit à cette promotion");
        }

        // Créer l'entrée dans STUDENT si elle n'existe pas
        if (!eleveRepository.existsById(eleveImmatriculation)) {
            
            // Insérer via SQL natif pour éviter le conflit Hibernate
            promotionRepository.insererEleveDansStudent(eleveImmatriculation);
        }

        // Insérer dans PROMOTION_STUDENT via SQL natif
        promotionRepository.insererDansPromotionStudent(promotionId, eleveImmatriculation);

        return promotionRepository.findById(promotionId).orElseThrow();
    }


    public Promotion desinscrireEleve(Integer promotionId, String eleveImmatriculation){
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));
        Eleve eleve = eleveRepository.findById(eleveImmatriculation)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
        promotion.getEleves().remove(eleve);
        return promotionRepository.save(promotion);
    }


    public List<EleveDTO> getElevesByPromotion(Integer promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable"));
        return promotion.getEleves().stream()
                .map(e -> new EleveDTO(e.getImmatriculation(), e.getNom(), e.getPrenom(), e.getEmail()))
                .toList();
    }
}