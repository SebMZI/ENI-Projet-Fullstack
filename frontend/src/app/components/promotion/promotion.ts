import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PromotionService } from '../../services/promotion/promotion.service';
import { CursusService } from '../../services/cursus/cursus.service';
import { PromotionDTO } from '../../interfaces/promotion';
import { CursusDTO } from '../../interfaces/cursus';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

@Component({
  selector: 'app-promotions',
  standalone: true,
  imports: [FormsModule, RouterModule, NavbarReferentComponent],
  templateUrl: './promotion.html',
  styleUrl: './promotion.css'
})
export class Promotions implements OnInit {

  private promotionService = inject(PromotionService);
  private cursusService = inject(CursusService);

  promotions = signal<PromotionDTO[]>([]);
  cursusList = signal<CursusDTO[]>([]);
  showModal = signal(false);
  nom = signal('');
  dateDebut = signal('');
  dateFin = signal('');
  selectedIdCursus: number | null = null;   
  modeEdition = signal(false);
  idEdition = signal<number | null>(null);
  erreur = signal<string | undefined>(undefined);

  ngOnInit() {
    this.charger();
    this.cursusService.getAll().subscribe({
      next: (data) => this.cursusList.set(data),
      error: () => this.erreur.set('Erreur chargement cursus')
    });
  }

  charger() {
    this.promotionService.getAll().subscribe({
      next: (data) => this.promotions.set(data),
      error: () => this.erreur.set('Erreur chargement promotions')
    });
  }

  ouvrirModal(promotion?: PromotionDTO) {
    if (promotion) {
      this.modeEdition.set(true);
      this.idEdition.set(promotion.id);
      this.nom.set(promotion.nom);
      this.dateDebut.set(promotion.dateDebut);
      this.dateFin.set(promotion.dateFin);
      this.selectedIdCursus = promotion.idCursus;  // ← ngModel
    } else {
      this.modeEdition.set(false);
      this.idEdition.set(null);
      this.nom.set('');
      this.dateDebut.set('');
      this.dateFin.set('');
      this.selectedIdCursus = null;
    }
    this.showModal.set(true);
  }

  fermerModal() {
    this.showModal.set(false);
  }

  getNomCursus(idCursus: number | null): string {
    const c = this.cursusList().find(c => c.id === idCursus);
    return c ? c.intitule : '—';
  }

  sauvegarder() {
    if (!this.nom().trim() || !this.dateDebut() || !this.dateFin() || !this.selectedIdCursus) {
      this.erreur.set('Tous les champs sont obligatoires');
      return;
    }

    const request = {
      nom: this.nom(),
      dateDebut: this.dateDebut(),
      dateFin: this.dateFin(),
      idCursus: this.selectedIdCursus  // ← ngModel
    };

    if (this.modeEdition() && this.idEdition() !== null) {
      this.promotionService.update(this.idEdition()!, request).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur modification')
      });
    } else {
      this.promotionService.create(request).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur création')
      });
    }
  }

  supprimer(id: number) {
    if (confirm('Supprimer cette promotion ?')) {
      this.promotionService.delete(id).subscribe({
        next: () => this.charger(),
        error: () => this.erreur.set('Erreur suppression')
      });
    }
  }
}