import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CoursPlanifieService } from '../../services/cours-planifie/cours-planifie.service';
import { PromotionService } from '../../services/promotion/promotion.service';
import { CursusService } from '../../services/cursus/cursus.service';
import { UtilisateurService, UtilisateurDTO } from '../../services/utilisateur/utilisateur.service';
import { CoursPlanifieDTO } from '../../interfaces/cours-planifie';
import { CoursDTO } from '../../interfaces/cours';
import { PromotionDTO } from '../../interfaces/promotion';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

interface LignePlanification {
  cours: CoursDTO;
  idPlanifie: number | null;
  dateDebut: string;
  dateFin: string;
  idFormateur: string | null;
  existant: boolean;
}

@Component({
  selector: 'app-cours-planifie',
  standalone: true,
  imports: [FormsModule, NavbarReferentComponent],
  templateUrl: './cours-planifie.html',
  styleUrl: './cours-planifie.css'
})
export class CoursPlanifie implements OnInit {

  private coursPlanifieService = inject(CoursPlanifieService);
  private promotionService = inject(PromotionService);
  private cursusService = inject(CursusService);
  private utilisateurService = inject(UtilisateurService);
  private route = inject(ActivatedRoute);

  promotionId!: number;
  promotion = signal<PromotionDTO | null>(null);
  lignes = signal<LignePlanification[]>([]);
  formateurs = signal<UtilisateurDTO[]>([]);
  message = signal<string | undefined>(undefined);
  erreur = signal<string | undefined>(undefined);

  // Modal
  showModal = signal(false);
  ligneEnEdition: LignePlanification | null = null;
  editDateDebut = signal('');
  editDateFin = signal('');
  editIdFormateur: string | null = null;

  ngOnInit() {
    this.promotionId = Number(this.route.snapshot.paramMap.get('id'));
    this.chargerDonnees();
    this.chargerFormateurs();
  }

  chargerFormateurs() {
    this.utilisateurService.getAll().subscribe({
      next: (data) => this.formateurs.set(data.filter(u => u.roles.includes('FORMATEUR'))),
      error: () => this.erreur.set('Erreur chargement formateurs')
    });
  }

  getNomFormateur(immatriculation: string | null): string {
    if (!immatriculation) return 'Non assigné';
    const f = this.formateurs().find(f => f.immatriculation === immatriculation);
    return f ? `${f.prenom} ${f.nom}` : immatriculation;
  }

  get nbPlanifies(): number {
    return this.lignes().filter(l => l.existant).length;
  }

  get nbAPlanifier(): number {
    return this.lignes().filter(l => !l.existant).length;
  }

  chargerDonnees() {
    this.message.set(undefined);
    this.erreur.set(undefined);
    this.promotionService.getById(this.promotionId).subscribe({
      next: (promo) => {
        this.promotion.set(promo);
        if (promo.idCursus) {
          this.chargerCoursEtPlanifies(promo.idCursus);
        } else {
          this.erreur.set('Cette promotion n\'a pas de cursus associé.');
        }
      },
      error: () => this.erreur.set('Promotion introuvable')
    });
  }

  chargerCoursEtPlanifies(idCursus: number) {
    this.cursusService.getCours(idCursus).subscribe({
      next: (cours) => {
        this.coursPlanifieService.getByPromotion(this.promotionId).subscribe({
          next: (planifies) => this.construireLignes(cours, planifies),
          error: () => this.erreur.set('Erreur chargement cours planifiés')
        });
      },
      error: () => this.erreur.set('Erreur chargement cours du cursus')
    });
  }

  construireLignes(cours: CoursDTO[], planifies: CoursPlanifieDTO[]) {
    const nouvellesLignes = cours.map(c => {
      const existant = planifies.find(p => p.titre === c.titre);
      return {
        cours: c,
        idPlanifie: existant ? existant.id : null,
        dateDebut: existant ? existant.dateDebut : '',
        dateFin: existant ? existant.dateFin : '',
        idFormateur: existant ? existant.idFormateur : null,
        existant: !!existant
      };
    });
    this.lignes.set(nouvellesLignes);
  }

  // ─── Modal ───
  ouvrirModal(ligne: LignePlanification) {
    this.ligneEnEdition = ligne;
    this.editDateDebut.set(ligne.dateDebut);
    this.editDateFin.set(ligne.dateFin);
    this.editIdFormateur = ligne.idFormateur;
    this.showModal.set(true);
  }

  fermerModal() {
    this.showModal.set(false);
    this.ligneEnEdition = null;
  }

  sauvegarderModal() {
    if (!this.ligneEnEdition) return;
    const request = {
      idCours: this.ligneEnEdition.cours.id,
      dateDebut: this.editDateDebut(),
      dateFin: this.editDateFin(),
      idFormateur: this.editIdFormateur ?? undefined
    };

    if (this.ligneEnEdition.existant && this.ligneEnEdition.idPlanifie !== null) {
      this.coursPlanifieService.update(this.ligneEnEdition.idPlanifie, request).subscribe({
        next: () => { this.message.set('Cours modifié !'); this.fermerModal(); this.chargerDonnees(); },
        error: () => this.erreur.set('Erreur modification')
      });
    } else {
      this.coursPlanifieService.create(this.promotionId, request).subscribe({
        next: () => { this.message.set('Cours planifié !'); this.fermerModal(); this.chargerDonnees(); },
        error: () => this.erreur.set('Erreur création')
      });
    }
  }
}