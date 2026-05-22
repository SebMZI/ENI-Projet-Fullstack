package fr.eni.backend.service;

import fr.eni.backend.bo.*;
import fr.eni.backend.dao.*;
import fr.eni.backend.dto.UtilisateurDTO;
import fr.eni.backend.dto.UtilisateurRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final EleveRepository eleveRepository;
    private final FormateurRepository formateurRepository;
    private final ReferenteAdministrativeRepository referenteRepository;
    private final AdministrateurRepository administrateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ──── Conversion entité → DTO ────
    private UtilisateurDTO toDTO(Utilisateur u) {
        String roleStr = u.getRoles().stream()
                .findFirst()
                .map(Role::getRole)
                .orElse(null);

        UtilisateurDTO.UtilisateurDTOBuilder builder = UtilisateurDTO.builder()
                .immatriculation(u.getImmatriculation())
                .nom(u.getNom())
                .prenom(u.getPrenom())
                .email(u.getEmail())
                .telephone(u.getTelephone())
                .dateCreation(u.getDateCreation())
                .role(roleStr);

        if (u instanceof Eleve e) {
            builder.emailPersonnel(e.getEmailPersonnel())
                   .dateInscription(e.getDateInscription());
        } else if (u instanceof Formateur f) {
            builder.statut(f.getStatut());
        } else if (u instanceof ReferenteAdministrative r) {
            builder.bureau(r.getBureau());
        } else if (u instanceof Administrateur a) {
            builder.service(a.getService());
        }
        return builder.build();
    }

    // ──── CRUD ────
    public List<UtilisateurDTO> findAll() {
        return utilisateurRepository.findAll().stream().map(this::toDTO).toList();
    }

    public UtilisateurDTO findById(String immatriculation) {
        Utilisateur u = utilisateurRepository.findById(immatriculation)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return toDTO(u);
    }

    public List<UtilisateurDTO> findElevesByPromotion(Integer idPromotion){
        return eleveRepository.findElevesByPromotion(idPromotion)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public UtilisateurDTO create(UtilisateurRequestDTO request) {
        if (utilisateurRepository.existsById(request.getImmatriculation())) {
            throw new RuntimeException("Immatriculation déjà utilisée");
        }

        String motDePasseEncode = passwordEncoder.encode(request.getMotDePasse());
        LocalDate aujourdHui = LocalDate.now();

        // Sauvegarder directement l'entité spécifique (la table mère USERS sera remplie automatiquement)
        Utilisateur saved = switch (request.getRole().toUpperCase()) {
            case "ELEVE" -> {
                Eleve e = Eleve.builder()
                        .immatriculation(request.getImmatriculation())
                        .nom(request.getNom()).prenom(request.getPrenom())
                        .email(request.getEmail()).motDePasse(motDePasseEncode)
                        .telephone(request.getTelephone()).dateCreation(aujourdHui)
                        .emailPersonnel(request.getEmailPersonnel())
                        .dateInscription(aujourdHui)
                        .build();
                yield eleveRepository.save(e);
            }
            case "FORMATEUR" -> {
                Formateur f = Formateur.builder()
                        .immatriculation(request.getImmatriculation())
                        .nom(request.getNom()).prenom(request.getPrenom())
                        .email(request.getEmail()).motDePasse(motDePasseEncode)
                        .telephone(request.getTelephone()).dateCreation(aujourdHui)
                        .statut(request.getStatut())
                        .build();
                yield formateurRepository.save(f);
            }
            case "REFERENTE" -> {
                ReferenteAdministrative r = ReferenteAdministrative.builder()
                        .immatriculation(request.getImmatriculation())
                        .nom(request.getNom()).prenom(request.getPrenom())
                        .email(request.getEmail()).motDePasse(motDePasseEncode)
                        .telephone(request.getTelephone()).dateCreation(aujourdHui)
                        .bureau(request.getBureau())
                        .build();
                yield referenteRepository.save(r);
            }
            case "ADMINISTRATEUR" -> {
                Administrateur a = Administrateur.builder()
                        .immatriculation(request.getImmatriculation())
                        .nom(request.getNom()).prenom(request.getPrenom())
                        .email(request.getEmail()).motDePasse(motDePasseEncode)
                        .telephone(request.getTelephone()).dateCreation(aujourdHui)
                        .service(request.getService())
                        .build();
                yield administrateurRepository.save(a);
            }
            default -> throw new RuntimeException("Rôle inconnu : " + request.getRole());
        };

        // Associer le rôle existant (ou le créer s'il n'existe pas)
        Role role = roleRepository.findByRole(request.getRole().toUpperCase())
                .orElseGet(() -> roleRepository.save(
                        Role.builder().role(request.getRole().toUpperCase()).build()));
        saved.getRoles().add(role);
        utilisateurRepository.save(saved);

        return toDTO(saved);
    }

    @Transactional
    public UtilisateurDTO update(String immatriculation, UtilisateurRequestDTO request) {
        Utilisateur u = utilisateurRepository.findById(immatriculation)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        u.setNom(request.getNom());
        u.setPrenom(request.getPrenom());
        u.setEmail(request.getEmail());
        u.setTelephone(request.getTelephone());
        if (request.getMotDePasse() != null && !request.getMotDePasse().isBlank()) {
            u.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        }
        utilisateurRepository.save(u);

        if (u instanceof Eleve e) {
            if (request.getEmailPersonnel() != null) e.setEmailPersonnel(request.getEmailPersonnel());
            eleveRepository.save(e);
        } else if (u instanceof Formateur f) {
            if (request.getStatut() != null) f.setStatut(request.getStatut());
            formateurRepository.save(f);
        } else if (u instanceof ReferenteAdministrative r) {
            if (request.getBureau() != null) r.setBureau(request.getBureau());
            referenteRepository.save(r);
        } else if (u instanceof Administrateur a) {
            if (request.getService() != null) a.setService(request.getService());
            administrateurRepository.save(a);
        }

        return toDTO(u);
    }

    @Transactional
    public void deleteById(String immatriculation) {
        Utilisateur u = utilisateurRepository.findById(immatriculation)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        // u.getRoles().clear();
        // utilisateurRepository.save(u);
        utilisateurRepository.delete(u);
    }
}