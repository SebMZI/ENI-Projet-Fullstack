package fr.eni.backend.service;

import fr.eni.backend.bo.*;
import fr.eni.backend.dao.*;
import java.util.List;
import fr.eni.backend.dto.InscriptionDTO;
import fr.eni.backend.dto.InscriptionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EleveRepository eleveRepository;
    private final CoursPlanifieRepository coursPlanifieRepository;

    @Transactional
    public InscriptionDTO inscrireUnite(InscriptionRequestDTO request, boolean forcee) {
    
        Eleve eleve = eleveRepository.findById(request.getImmatriculation())
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));

    
        CoursPlanifie cp = coursPlanifieRepository.findById(request.getCoursPlanifieId())
                .orElseThrow(() -> new RuntimeException("Cours planifié introuvable"));

        // Vérifier le doublon
        if (inscriptionRepository.existsByEleveImmatriculationAndCoursPlanifieId(
                request.getImmatriculation(), request.getCoursPlanifieId())) {
            throw new RuntimeException("Élève déjà inscrit à ce cours");
        }

        // Vérifier l'ordre (sauf si forcé)
        if (!forcee) {
            verifierOrdre(eleve, cp);
        }

        // Créer l'inscription
        Inscription inscription = Inscription.builder()
                .dateInscription(LocalDate.now())
                .type("UNITE")
                .forcee(forcee)
                .eleve(eleve)
                .coursPlanifie(cp)
                .build();

        Inscription saved = inscriptionRepository.save(inscription);

        return InscriptionDTO.builder()
                .id(saved.getId())
                .dateInscription(saved.getDateInscription())
                .type(saved.getType())
                .forcee(saved.isForcee())
                .eleveImmatriculation(eleve.getImmatriculation())
                .coursPlanifieId(cp.getId())
                .titreCours(cp.getTitre())
                .build();
    }


    private void verifierOrdre(Eleve eleve, CoursPlanifie cp)    // coursPlanifie
    {
    
        // 1. Remonter au cursus
        Promotion promotion = cp.getPromotion();
        if (promotion == null || promotion.getCursus() == null) {
            return; 
        }

        // 2. Trouver l'ordre du cours demandé
        Cursus cursus = promotion.getCursus();


        Cours coursDuCursus = cursus.getCours().stream()
                .filter(c -> c.getTitre().equals(cp.getTitre()))
                .findFirst()
                .orElse(null);
        if (coursDuCursus == null) return;

        // ex: 2 pour JavaScript
        int ordreCours = coursDuCursus.getOrdre() != null ? coursDuCursus.getOrdre() : 0;

        // 3. Lister les prérequis (ordre inférieur)
        List<Cours> coursPrerequis = cursus.getCours().stream()
                .filter(c -> c.getOrdre() != null && c.getOrdre() < ordreCours)
                .toList();

    
        for (Cours prerequis : coursPrerequis) {
            // Vérifier via les inscriptions à l'unité
            boolean inscritUnite = inscriptionRepository
                    .existsByEleveImmatriculationAndCoursPlanifieTitre(
                            eleve.getImmatriculation(), prerequis.getTitre());

        
            boolean inscritPromo = promotion.getEleves().stream()
                    .anyMatch(e -> e.getImmatriculation().equals(eleve.getImmatriculation()));

            if (!inscritUnite && !inscritPromo) {
                throw new RuntimeException(
                    "L'élève doit d'abord suivre le cours : " + prerequis.getTitre() +
                    " (ordre " + prerequis.getOrdre() + "). Utilisez le forçage pour passer outre."
                );
            }
        }
    }


    public List<InscriptionDTO> findByEleve(String immatriculation) {
        return inscriptionRepository.findByEleveImmatriculation(immatriculation)
                .stream().map(i -> InscriptionDTO.builder()
                    .id(i.getId())
                    .dateInscription(i.getDateInscription())
                    .type(i.getType())
                    .forcee(i.isForcee())
                    .eleveImmatriculation(i.getEleve().getImmatriculation())
                    .coursPlanifieId(i.getCoursPlanifie() != null ? i.getCoursPlanifie().getId() : null)
                    .titreCours(i.getCoursPlanifie() != null ? i.getCoursPlanifie().getTitre() : null)
                    .build())
                .toList();
    }
}