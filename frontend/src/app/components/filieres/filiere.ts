import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FiliereService } from '../../services/filiere/filiere.service';
import { FiliereDTO } from '../../interfaces/filiere';
import { RouterModule } from '@angular/router';
import { NavbarReferentComponent } from '../navbar-referent/navbar-referent';

@Component({
  selector: 'app-filiere-list',
  standalone: true,
  imports: [FormsModule, RouterModule, NavbarReferentComponent], 
  templateUrl: './filiere.html',
  styleUrl: './filiere.css'
})
export class Filiere implements OnInit {

  private filiereService = inject(FiliereService);

  filieres = signal<FiliereDTO[]>([]);
  showModal = signal(false);
  nomFiliere = signal('');
  modeEdition = signal(false);
  idEdition = signal<number | null>(null);
  erreur = signal<string | undefined>(undefined);

  ngOnInit() {
    this.charger();
  }

  charger() {
    this.filiereService.getAll().subscribe({
      next: (data) => this.filieres.set(data),
      error: () => this.erreur.set('Erreur chargement filières')
    });
  }

  ouvrirModal(filiere?: FiliereDTO) {
    if (filiere) {
      // Mode modification
      this.modeEdition.set(true);
      this.idEdition.set(filiere.id);
      this.nomFiliere.set(filiere.nom);
    } else {
      // Mode création
      this.modeEdition.set(false);
      this.idEdition.set(null);
      this.nomFiliere.set('');
    }
    this.showModal.set(true);
  }

  fermerModal() {
    this.showModal.set(false);
    this.nomFiliere.set('');
  }

  sauvegarder() {
    if (!this.nomFiliere().trim()) return;

    if (this.modeEdition() && this.idEdition() !== null) {
      // PUT — modifier
      this.filiereService.update(this.idEdition()!, { nom: this.nomFiliere() }).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur modification')
      });
    } else {
      // POST — créer
      this.filiereService.create({ nom: this.nomFiliere() }).subscribe({
        next: () => { this.charger(); this.fermerModal(); },
        error: () => this.erreur.set('Erreur création')
      });
    }
  }

  supprimer(id: number) {
    if (confirm('Supprimer cette filière ?')) {
      this.filiereService.delete(id).subscribe({
        next: () => this.charger(),
        error: () => this.erreur.set('Erreur suppression')
      });
    }
  }
}