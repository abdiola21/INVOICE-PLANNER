import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoginRequest } from '../../../models/requests/Auth/login-request';
import { AuthService } from '../../../services/Auth/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  isLoading = false;
  errorMessage = '';
  returnUrl: string = '/';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
  }

  initForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const loginData: LoginRequest = {
      email: this.loginForm.get('email')?.value,
      password: this.loginForm.get('password')?.value
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (!response.error) {
          this.messageService.add({
            severity: 'success',
            summary: 'Connexion réussie',
            detail: 'Vous êtes maintenant connecté',
            life: 3000 // La notification disparaît après 3 secondes
          });

          setTimeout(() => {
            this.router.navigate([this.returnUrl]);
          }, 3000);
        } else {
          this.showErrorAlert(response.message);
        }
      },
      error: (error) => {
        this.isLoading = false;
        const errorMessage = error.error?.message || "Une erreur s'est produite lors de la connexion";
        this.showErrorAlert(errorMessage);
      }
    });
  }

  showErrorAlert(message: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Erreur de connexion',
      text: message,
      confirmButtonColor: '#d33'
    });
  }
}

