import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UtilisateurService, UtilisateurDTO } from '../../services/utilisateur/utilisateur.service';
import { PromotionService } from '../../services/promotion/promotion.service';
import { PromotionDTO } from '../../interfaces/promotion';
import { CoursPlanifieService } from '../../services/cours-planifie/cours-planifie.service';
import { CoursPlanifieDTO } from '../../interfaces/cours-planifie';
import { InscriptionService, InscriptionDTO } from '../../services/inscription/inscription.service';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

@Component({
  selector: 'app-eleves',
  standalone: true,
  imports: [FormsModule, NavbarReferentComponent],
  templateUrl: './eleves.html',
  styleUrl: './eleves.css'
})
export class Eleves implements OnInit {

  private utilisateurService = inject(UtilisateurService);
  private promotionService = inject(PromotionService);
  private coursPlanifieService = inject(CoursPlanifieService);
  private inscriptionService = inject(InscriptionService);

  eleves = signal<UtilisateurDTO[]>([]);
  promotions = signal<PromotionDTO[]>([]);
  promosByEleve = signal<Map<string, PromotionDTO[]>>(new Map());
  inscriptionsByEleve = signal<Map<string, InscriptionDTO[]>>(new Map());

  // Modal promotion
  showModalPromo = signal(false);
  selectedEleveImmatriculation: string | null = null;
  selectedEleveNom: string = '';
  selectedIdPromotion: number | null = null;

  // Modal cours à l'unité
  showModalCours = signal(false);
  coursPlanifies = signal<CoursPlanifieDTO[]>([]);
  selectedIdCoursPlanifie: number | null = null;

  // Modal liste des cours à l'unité
  showModalListeCours = signal(false);
  eleveListeCours: string = '';
  coursUniteList: InscriptionDTO[] = [];

  message = signal<string | undefined>(undefined);
  erreur = signal<string | undefined>(undefined);

  ngOnInit() {
    this.chargerEleves();
    this.chargerPromotions();
  }

  chargerEleves() {
    this.utilisateurService.getAll().subscribe({
      next: (data) => {
        const elevesData = data.filter(u => u.roles.includes('ELEVE'));
        this.eleves.set(elevesData);
        elevesData.forEach(eleve => {
          // Charger les promos
          this.promotionService.getByEleve(eleve.immatriculation).subscribe({
            next: (promos) => {
              const map = new Map(this.promosByEleve());
              map.set(eleve.immatriculation, promos);
              this.promosByEleve.set(map);
            }
          });
          // Charger les inscriptions à l'unité
          this.inscriptionService.getByEleve(eleve.immatriculation).subscribe({
            next: (inscriptions) => {
              const map = new Map(this.inscriptionsByEleve());
              map.set(eleve.immatriculation, inscriptions.filter(i => i.type === 'UNITE'));
              this.inscriptionsByEleve.set(map);
            }
          });
        });
      },
      error: () => this.erreur.set('Erreur chargement élèves')
    });
  }

  getPromosOfEleve(immatriculation: string): PromotionDTO[] {
    return this.promosByEleve().get(immatriculation) || [];
  }

  getNomPromo(immatriculation: string): string {
    const promos = this.getPromosOfEleve(immatriculation);
    return promos.length > 0 ? promos.map(p => p.nom).join(', ') : 'Aucune';
  }

  getNbCoursUnite(immatriculation: string): number {
    return (this.inscriptionsByEleve().get(immatriculation) || []).length;
  }

  chargerPromotions() {
    this.promotionService.getAll().subscribe({
      next: (data) => this.promotions.set(data),
      error: () => this.erreur.set('Erreur chargement promotions')
    });
  }

  // ─── Modal Promotion ───
  ouvrirModalPromo(eleve: UtilisateurDTO) {
    this.selectedEleveImmatriculation = eleve.immatriculation;
    this.selectedEleveNom = `${eleve.prenom} ${eleve.nom}`;
    this.selectedIdPromotion = null;
    this.message.set(undefined);
    this.erreur.set(undefined);
    this.showModalPromo.set(true);
  }

  fermerModalPromo() {
    this.showModalPromo.set(false);
  }

  inscrirePromo() {
    if (!this.selectedEleveImmatriculation || !this.selectedIdPromotion) {
      this.erreur.set('Veuillez choisir une promotion');
      return;
    }
    this.promotionService.inscrireEleve(this.selectedIdPromotion, this.selectedEleveImmatriculation).subscribe({
      next: () => {
        this.message.set('Élève inscrit avec succès !');
        this.promotionService.getByEleve(this.selectedEleveImmatriculation!).subscribe(promos => {
          const map = new Map(this.promosByEleve());
          map.set(this.selectedEleveImmatriculation!, promos);
          this.promosByEleve.set(map);
        });
        setTimeout(() => this.fermerModalPromo(), 1500);
      },
      error: (err) => {
        this.message.set(err.error?.message || 'Erreur inscription');
      }
    });
  }

  // ─── Modal Cours à l'unité ───
  ouvrirModalCours(eleve: UtilisateurDTO) {
    this.selectedEleveImmatriculation = eleve.immatriculation;
    this.selectedEleveNom = `${eleve.prenom} ${eleve.nom}`;
    this.selectedIdCoursPlanifie = null;
    this.message.set(undefined);
    this.erreur.set(undefined);
    this.coursPlanifieService.getAll().subscribe({
      next: (data) => this.coursPlanifies.set(data),
      error: () => this.erreur.set('Erreur chargement cours')
    });
    this.showModalCours.set(true);
  }

  fermerModalCours() {
    this.showModalCours.set(false);
  }

  inscrireCours() {
    if (!this.selectedEleveImmatriculation || !this.selectedIdCoursPlanifie) {
      this.erreur.set('Veuillez choisir un cours');
      return;
    }
    const request = {
      immatriculation: this.selectedEleveImmatriculation,
      coursPlanifieId: this.selectedIdCoursPlanifie
    };
    this.inscriptionService.inscrireUnite(request).subscribe({
      next: () => {
        this.message.set('Élève inscrit au cours avec succès !');
        this.actualiserInscriptions(this.selectedEleveImmatriculation!);
        setTimeout(() => this.fermerModalCours(), 1500);
      },
      error: (err) => {
        this.message.set(err.error?.message || 'Erreur inscription');
      }
    });
  }

  inscrireCoursForcee() {
    if (!this.selectedEleveImmatriculation || !this.selectedIdCoursPlanifie) {
      this.erreur.set('Veuillez choisir un cours');
      return;
    }
    const request = {
      immatriculation: this.selectedEleveImmatriculation,
      coursPlanifieId: this.selectedIdCoursPlanifie
    };
    this.inscriptionService.inscrireUniteForcee(request).subscribe({
      next: () => {
        this.message.set('Élève inscrit au cours (forcé) avec succès !');
        this.actualiserInscriptions(this.selectedEleveImmatriculation!);
        setTimeout(() => this.fermerModalCours(), 1500);
      },
      error: (err) => {
        this.message.set(err.error?.message || 'Erreur inscription forcée');
      }
    });
  }

  actualiserInscriptions(immatriculation: string) {
    this.inscriptionService.getByEleve(immatriculation).subscribe(inscriptions => {
      const map = new Map(this.inscriptionsByEleve());
      map.set(immatriculation, inscriptions.filter(i => i.type === 'UNITE'));
      this.inscriptionsByEleve.set(map);
    });
  }

  // ─── Modal Liste des cours à l'unité ───
  ouvrirModalListeCours(immatriculation: string, nom: string) {
    this.eleveListeCours = nom;
    this.coursUniteList = this.inscriptionsByEleve().get(immatriculation) || [];
    this.showModalListeCours.set(true);
  }

  fermerModalListeCours() {
    this.showModalListeCours.set(false);
  }
}