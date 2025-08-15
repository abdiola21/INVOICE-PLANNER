import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { ClientRequest } from '../../../../../../models/requests/Clients/client-request';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import Swal from 'sweetalert2';
import { MessageService } from 'primeng/api';
import { catchError, finalize, of } from 'rxjs';
import { CommonModule, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-edit-client',
  templateUrl: './edit-client.component.html',
  styleUrls: ['./edit-client.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterModule, NgIf],
  providers: [MessageService]
})
export class EditClientComponent implements OnInit {
  clientForm!: FormGroup;
  clientId!: string;
  isLoading = false;
  loadingData = false;
  errorMessage = '';
  client: ClientResponse | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private clientService: ClientsService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    // Initialiser le formulaire
    this.initForm();
    
    // Récupérer l'ID du client dans l'URL
    this.clientId = this.route.snapshot.paramMap.get('id')!;
    
    if (this.clientId) {
      this.loadClientData();
    } else {
      this.errorMessage = "ID client non spécifié";
      this.showError("ID client non spécifié");
    }
  }

  private initForm(): void {
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

  private loadClientData(): void {
    this.loadingData = true;
    console.log('Chargement des données du client ID:', this.clientId);
    
    this.clientService.getClientById(this.clientId)
      .pipe(
        catchError(error => {
          console.error('Erreur lors du chargement des données client:', error);
          this.errorMessage = "Impossible de charger les données du client";
          this.showError(this.errorMessage);
          return of(null);
        }),
        finalize(() => this.loadingData = false)
      )
      .subscribe(client => {
        if (client) {
          console.log('Données client reçues:', client);
          this.client = client;
          this.patchFormValues(client);
        }
      });
  }

  private patchFormValues(client: ClientResponse): void {
    this.clientForm.patchValue({
      nom: client.nom || '',
      adresse: client.adresse || '',
      email: client.email || '',
      telephone: client.telephone || '',
      societe: client.societe || '',
      numeroTVA: client.numeroTVA || '',
      ville: client.ville || '',
      pays: client.pays || ''
    });
  }

  onUpdate(): void {
    if (this.clientForm.invalid) {
      this.markAllFieldsAsTouched();
      return;
    }

    const clientData: ClientRequest = this.clientForm.value;
    console.log('Données à mettre à jour:', clientData);
    
    this.isLoading = true;
    this.errorMessage = '';

    this.clientService.updateClient(this.clientId, clientData)
      .pipe(
        catchError(error => {
          console.error('Erreur lors de la mise à jour du client:', error);
          this.errorMessage = "Une erreur s'est produite lors de la modification du client";
          this.showError(this.errorMessage);
          return of(null);
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe(result => {
        if (result) {
          this.showSuccess();
          this.router.navigate(['/dashboard/clients/details', this.clientId]);
        }
      });
  }

  private markAllFieldsAsTouched(): void {
    Object.keys(this.clientForm.controls).forEach(key => {
      this.clientForm.get(key)?.markAsTouched();
    });
  }

  private showSuccess(): void {
    Swal.fire({
      title: 'Modification réussie',
      text: 'Le client a été modifié avec succès',
      icon: 'success',
      confirmButtonText: 'OK'
    });
    
    this.messageService.add({
      severity: 'success',
      summary: 'Modification réussie',
      detail: 'Le client a été modifié avec succès'
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
}

