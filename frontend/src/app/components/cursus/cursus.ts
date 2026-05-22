import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CursusService } from '../../services/cursus/cursus.service';
import { FiliereService } from '../../services/filiere/filiere.service';
import { CursusDTO } from '../../interfaces/cursus';
import { FiliereDTO } from '../../interfaces/filiere';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

@Component({
  selector: 'app-cursus',
  standalone: true,
  imports: [FormsModule, RouterModule, NavbarReferentComponent],
  templateUrl: './cursus.html',
  styleUrl: './cursus.css'
})
export class Cursus implements OnInit {

  private cursusService = inject(CursusService);
  private filiereService = inject(FiliereService);
  private route = inject(ActivatedRoute);

  cursusList = signal<CursusDTO[]>([]);
  filieres = signal<FiliereDTO[]>([]);
  filiereId: number | null = null;
  showModal = signal(false);
  intitule = signal('');
  selectedIdFiliere: number | null = null;
  modeEdition = signal(false);
  idEdition = signal<number | null>(null);
  erreur = signal<string | undefined>(undefined);

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.filiereId = params['filiereId'] ? Number(params['filiereId']) : null;
      this.charger();
    });
    this.filiereService.getAll().subscribe({
      next: (data) => this.filieres.set(data),
      error: () => this.erreur.set('Erreur chargement filières')
    });
  }

  charger() {
    this.cursusService.getAll().subscribe({
      next: (data) => {
        if (this.filiereId) {
          this.cursusList.set(data.filter(c => c.idFiliere === this.filiereId));
        } else {
          this.cursusList.set(data);
        }
      },
      error: () => this.erreur.set('Erreur chargement cursus')
    });
  }

  getNomFiliere(idFiliere: number | null): string {
    const f = this.filieres().find(f => f.id === idFiliere);
    return f ? f.nom : '—';
  }

  ouvrirModal(cursus?: CursusDTO) {
    if (cursus) {
      this.modeEdition.set(true);
      this.idEdition.set(cursus.id);
      this.intitule.set(cursus.intitule);
      this.selectedIdFiliere = cursus.idFiliere;
    } else {
      this.modeEdition.set(false);
      this.idEdition.set(null);
      this.intitule.set('');
      this.selectedIdFiliere = this.filiereId;
    }
    this.showModal.set(true);
  }

  fermerModal() {
    this.showModal.set(false);
  }

  sauvegarder() {
    if (!this.intitule().trim() || !this.selectedIdFiliere) {
      this.erreur.set('Tous les champs sont obligatoires');
      return;
    }

    const request = {
      intitule: this.intitule(),
      idFiliere: this.selectedIdFiliere
    };

    if (this.modeEdition() && this.idEdition() !== null) {
      this.cursusService.update(this.idEdition()!, request).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur modification')
      });
    } else {
      this.cursusService.create(request).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur création')
      });
    }
  }

  supprimer(id: number) {
    if (confirm('Supprimer ce cursus ?')) {
      this.cursusService.delete(id).subscribe({
        next: () => this.charger(),
        error: () => this.erreur.set('Erreur suppression')
      });
    }
  }
}