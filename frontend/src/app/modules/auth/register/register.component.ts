import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { RegisterRequest } from '../../../models/requests/Auth/register-request';
import { AuthService } from '../../../services/Auth/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  passwordsNotMatching = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.registerForm = this.formBuilder.group({
      prenom: ['', [Validators.required]],
      nom: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      numeroDeTelephone: [''],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      acceptTerms: [false, [Validators.requiredTrue]],
    });

    // Vérification des mots de passe en temps réel
    this.registerForm.get('confirmPassword')?.valueChanges.subscribe(() => {
      this.checkPasswordsMatch();
    });
    this.registerForm.get('password')?.valueChanges.subscribe(() => {
      if (this.registerForm.get('confirmPassword')?.value) {
        this.checkPasswordsMatch();
      }
    });
  }

  checkPasswordsMatch(): void {
    const password = this.registerForm.get('password')?.value;
    const confirmPassword = this.registerForm.get('confirmPassword')?.value;
    this.passwordsNotMatching = password !== confirmPassword;
  }

  onSubmit(): void {
    if (this.registerForm.invalid || this.passwordsNotMatching) {
      // Marquer tous les champs comme touchés pour afficher les erreurs
      Object.keys(this.registerForm.controls).forEach((key) => {
        this.registerForm.get(key)?.markAsTouched();
      });

      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: 'Veuillez remplir correctement tous les champs du formulaire.',
      });

      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const registerData: RegisterRequest = {
      prenom: this.registerForm.get('prenom')?.value,
      nom: this.registerForm.get('nom')?.value,
      email: this.registerForm.get('email')?.value,
      password: this.registerForm.get('password')?.value,
      confirmPassword: this.registerForm.get('confirmPassword')?.value,
      numeroDeTelephone:
        this.registerForm.get('numeroDeTelephone')?.value || undefined,
    };

    this.authService.register(registerData).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (!response.error) {
          Swal.fire({
            icon: 'success',
            title: 'Inscription réussie',
            text: 'Votre compte a été créé avec succès ! Un lien d\'activation vous a été envoyé à votre email.',
            confirmButtonText: 'OK',
          }).then(() => {
            this.router.navigate(['/auth']);
          });
        } else {
          this.errorMessage = "Une erreur s'est produite lors de l'inscription";
          Swal.fire({
            icon: 'error',
            title: "Erreur d'inscription",
            text: this.errorMessage,
          });
          console.error(response.error);
        }
      },
      error: (error) => {
        this.isLoading = false;
        console.error(error.error?.message);
        this.errorMessage = "Une erreur s'est produite lors de l'inscription";
        Swal.fire({
          icon: 'error',
          title: "Erreur d'inscription",
          text: this.errorMessage,
        });
      },
    });
  }
}

