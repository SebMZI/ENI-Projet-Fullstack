import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {AuthenticationService} from '../../services/authentication/authentication.service';
import { AuthRequest } from '../../interfaces/authRequest';
import { lastValueFrom } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Utilisateur } from '../../interfaces/utilisateur';
import { AuthResponse } from '../../interfaces/auth-response';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-authentication',
  imports: [ReactiveFormsModule],
  templateUrl: './authentication.html',
  styleUrl: './authentication.css',
})
export class Authentication {
  private router = inject(Router);
  private authService = inject(AuthenticationService);

  public errorMsg = signal<string | undefined>(undefined);

  public authForm = new FormGroup({
    pseudo: new FormControl('', [
      Validators.email,
      Validators.maxLength(150),
      Validators.pattern('^[\\w-\\.]+@campus-eni.fr$'),
      Validators.required,
    ]),
    password: new FormControl('', [Validators.maxLength(255), Validators.required]),
  });

  async onSubmit() {
    if (this.authForm.invalid) {
      console.log('Authentication failed');
      return;
    }
    try {
      const response: AuthResponse = await lastValueFrom(
        this.authService.authenticate(<AuthRequest> this.authForm.value),
      );

      console.log('Auth', response);
      this.authService.utilisateur = response.utilisateur;
      this.authService.setTokenInStorage(response.token);
      await this.router.navigate(['/']);
    } catch (e: unknown) {
      const error = e as HttpErrorResponse;
      console.log('Error', error);

      if (error.status === 403) {
        console.error(error.status);
        this.errorMsg.set('Failed to authenticate');
        setTimeout(() => {
          this.errorMsg.set(undefined);
        }, 3000);
      }
    }
  }

  get pseudo() {
    return this.authForm.get('pseudo');
  }

  get password() {
    return this.authForm.get('password');
  }
}
