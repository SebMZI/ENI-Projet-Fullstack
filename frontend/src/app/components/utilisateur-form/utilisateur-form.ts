import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { Utilisateur } from '../../interfaces/utilisateur';
import { CustomMultipleSelect } from '../custom-multiple-select/custom-multiple-select';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Utilisateurs } from '../utilisateurs/utilisateurs';

@Component({
  selector: 'app-utilisateur-form',
  imports: [CustomMultipleSelect, FormsModule, ReactiveFormsModule],
  templateUrl: './utilisateur-form.html',
  styleUrl: './utilisateur-form.css',
})
export class UtilisateurForm {
  @Input() errorMsg: string | undefined;
  @Input() createdUser: Utilisateur | undefined;
  public roles: string[] = ['Eleve', 'Formateur', 'Referente', 'Administrateur'];

  public form: FormGroup = new FormGroup({
    nom: new FormControl('', Validators.required),
    prenom: new FormControl('', Validators.required),
    email: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.pattern('^[\\w-\\.]+@campus-eni.fr$'),
    ]),
    telephone: new FormControl('', [Validators.pattern(/^\d{10}$/)]),
    bureau: new FormControl(''),
    statut: new FormControl(''),
    service: new FormControl(''),
    emailPersonnel: new FormControl(''),
    adresse: new FormGroup({
      rue: new FormControl(''),
      codePostal: new FormControl(''),
      ville: new FormControl(''),
    }),
  });

  public rolesSignal = signal<string[]>([]);

  private _utilisateur?: Utilisateur;
  private _isAdd = true;

  @Input()
  set utilisateur(user: Utilisateur | undefined) {
    this._utilisateur = user;
    this._isAdd = !user;

    if (user) {
      this.form.patchValue({
        immatriculation: user.immatriculation,
        nom: user.nom,
        prenom: user.prenom,
        email: user.email,
        telephone: user.telephone ?? '',
        bureau: user.bureau ?? '',
        statut: user.statut ?? '',
        service: user.service ?? '',
        emailPersonnel: user.emailPersonnel ?? '',
        adresse: {
          rue: user.adresse?.rue ?? '',
          codePostal: user.adresse?.codePostal ?? '',
          ville: user.adresse?.ville ?? '',
        },
      });

      if (!this._isAdd) {
        this.form.controls['nom'].disable();
        this.form.controls['prenom'].disable();
        this.form.controls['email'].disable();
        this.form.controls['telephone'].disable();
        this.form.controls['bureau'].disable();
        this.form.controls['statut'].disable();
        this.form.controls['service'].disable();
        this.form.controls['emailPersonnel'].disable();
        (this.form.controls['adresse'] as FormGroup).disable();
      } else {
        this.form.enable();
      }

      this.rolesSignal.set(user.roles ?? []);
    }
  }

  @Output() newUser = new EventEmitter<Utilisateur>();

  public setNewUser() {
    if (this.form.invalid || !this.checkRoles(this.rolesSignal())) {
      Object.keys(this.form.controls).forEach((key) => {
        const control = this.form.get(key);

        if (control?.invalid) {
          console.log('Champ invalide:', key);
          console.log('Erreurs:', control.errors);
          console.log('Valeur:', control.value);
        }
      });

      console.log('Form errors:', this.form.errors);
      return;
    }

    console.log('EDOENNE');
    this.newUser.emit({
      ...this.form.value,
      roles: this.rolesSignal(),
    });
  }

  public getRoles(): string[] {
    return this.roles;
  }

  public isAdd(): boolean {
    return this._isAdd;
  }

  public hasUserRole(role: string) {
    return this.rolesSignal().includes(role);
  }

  public selectedRole(role: string) {
    role = `ROLE_${role.toUpperCase()}`;
    const currentRoles = this.rolesSignal();

    if (currentRoles.includes(role)) return;
    if (role === 'ROLE_ELEVE' && currentRoles.length > 0) return;
    if (role !== 'ROLE_ELEVE' && currentRoles.includes('ROLE_ELEVE')) return;

    this.rolesSignal.set([...currentRoles, role]);
  }

  public removeRole(role: string) {
    this.rolesSignal.set(this.rolesSignal().filter((r) => r !== role));
  }

  onlyPhoneNumbers(event: Event): void {
    const input = (event.target as HTMLInputElement).value.replace(/\D/g, '').slice(0, 10);

    this.form.get('telephone')?.setValue(input, {
      emitEvent: false,
    });
  }

  onlyNumbers(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/\D/g, '').slice(0, 5);
  }

  private checkRoles(roles: string[]) {
    if (!roles || roles?.length < 1) return false;
    return true;
  }
}
