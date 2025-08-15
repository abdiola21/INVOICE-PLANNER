import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { ClientRequest } from '../../../../../../models/requests/Clients/client-request';
import Swal from 'sweetalert2';
import { MessageService } from 'primeng/api';
import { catchError, finalize, of } from 'rxjs';
import { CommonModule, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-add-client',
  templateUrl: './add-client.component.html',
  styleUrls: ['./add-client.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule, NgIf],
  providers: [MessageService]
})
export class AddClientComponent implements OnInit {
  clientForm!: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private clientService: ClientsService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  private initForm(): void {
    // Initialiser le formulaire avec des validations
    this.clientForm = this.fb.group({
      nom: ['', Validators.required],
      adresse: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', Validators.required],
      societe: ['', Validators.required],
      numeroTVA: ['', Validators.required],
      ville: ['', Validators.required],
      pays: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.clientForm.invalid) {
      this.markAllFieldsAsTouched();
      return;
    }

    const clientData: ClientRequest = this.clientForm.value;
    console.log('Données client à ajouter:', clientData);
    
    this.isLoading = true;
    this.errorMessage = '';

    this.clientService.createClient(clientData)
      .pipe(
        catchError(error => {
          console.error('Erreur lors de l\'ajout du client:', error);
          this.errorMessage = "Une erreur s'est produite lors de l'ajout du client";
          this.showError(this.errorMessage);
          return of(null);
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe(result => {
        if (result) {
          this.showSuccess();
          this.router.navigate(['/dashboard/clients']);
        }
      });
  }

  private markAllFieldsAsTouched(): void {
    // Marquer tous les champs comme touchés pour afficher les erreurs de validation
    Object.keys(this.clientForm.controls).forEach(key => {
      this.clientForm.get(key)?.markAsTouched();
    });
  }

  private showSuccess(): void {
    Swal.fire({
      title: 'Ajout réussi',
      text: 'Le client a été ajouté avec succès',
      icon: 'success',
      confirmButtonText: 'OK'
    });
    
    this.messageService.add({
      severity: 'success',
      summary: 'Ajout réussi',
      detail: 'Le client a été ajouté avec succès'
    });
  }

  private showError(message: string): void {
    Swal.fire({
      title: 'Erreur',
      text: message,
      icon: 'error',
      confirmButtonText: 'OK'
    });
    
    this.messageService.add({
      severity: 'error',
      summary: 'Erreur',
      detail: message
    });
  }

  // Méthode pour annuler et retourner à la liste des clients
  onCancel(): void {
    this.router.navigate(['/dashboard/clients']);
  }

  // Méthode pour réinitialiser le formulaire
  onReset(): void {
    this.clientForm.reset();
    this.errorMessage = '';
  }
}

