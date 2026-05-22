import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CoursService } from '../../services/cours/cours.service';
import { CursusService } from '../../services/cursus/cursus.service';
import { CoursDTO } from '../../interfaces/cours';
import { CursusDTO } from '../../interfaces/cursus';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

@Component({
  selector: 'app-cours',
  standalone: true,
  imports: [FormsModule, RouterModule, NavbarReferentComponent],
  templateUrl: './cours.html',
  styleUrl: './cours.css'
})
export class Cours implements OnInit {

  private coursService = inject(CoursService);
  private cursusService = inject(CursusService);
  private route = inject(ActivatedRoute);

  coursList = signal<CoursDTO[]>([]);
  cursusList = signal<CursusDTO[]>([]);
  cursusId: number | null = null;
  showModal = signal(false);
  titre = signal('');
  duree = signal(0);
  ordre = signal(1);
  selectedIdCursus: number | null = null;
  modeEdition = signal(false);
  idEdition = signal<number | null>(null);
  erreur = signal<string | undefined>(undefined);

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.cursusId = params['cursusId'] ? Number(params['cursusId']) : null;
      this.charger();
    });
    this.cursusService.getAll().subscribe({
      next: (data) => this.cursusList.set(data),
      error: () => this.erreur.set('Erreur chargement cursus')
    });
  }

  charger() {
    this.coursService.getAll().subscribe({
      next: (data) => {
        let filtered = this.cursusId ? data.filter(c => c.idCursus === this.cursusId) : data;
        // Trier par cursus puis par ordre
        filtered.sort((a, b) => {
          if (a.idCursus !== b.idCursus) return (a.idCursus ?? 0) - (b.idCursus ?? 0);
          return (a.ordre ?? 99) - (b.ordre ?? 99);
        });
        this.coursList.set(filtered);
      },
      error: () => this.erreur.set('Erreur chargement cours')
    });
  }

  getNomCursus(idCursus: number | null): string {
    const c = this.cursusList().find(c => c.id === idCursus);
    return c ? c.intitule : '—';
  }

  ouvrirModal(cours?: CoursDTO) {
    if (cours) {
      this.modeEdition.set(true);
      this.idEdition.set(cours.id);
      this.titre.set(cours.titre);
      this.duree.set(cours.duree);
      this.ordre.set(cours.ordre ?? 1);
      this.selectedIdCursus = cours.idCursus;
    } else {
      this.modeEdition.set(false);
      this.idEdition.set(null);
      this.titre.set('');
      this.duree.set(0);
      this.ordre.set(1);
      this.selectedIdCursus = this.cursusId;
    }
    this.showModal.set(true);
  }

  fermerModal() {
    this.showModal.set(false);
  }

  sauvegarder() {
    if (!this.titre().trim() || this.duree() <= 0 || !this.selectedIdCursus) {
      this.erreur.set('Tous les champs sont obligatoires');
      return;
    }

    const request = {
      titre: this.titre(),
      duree: this.duree(),
      idCursus: this.selectedIdCursus,
      ordre: this.ordre()
    };

    if (this.modeEdition() && this.idEdition() !== null) {
      this.coursService.update(this.idEdition()!, request).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur modification')
      });
    } else {
      this.coursService.create(request).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur création')
      });
    }
  }

  supprimer(id: number) {
    if (confirm('Supprimer ce cours ?')) {
      this.coursService.delete(id).subscribe({
        next: () => this.charger(),
        error: () => this.erreur.set('Erreur suppression')
      });
    }
  }
}