import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DevisService } from '../../../../../../services/devis/devis.service';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { PrestationsService } from '../../../../../../services/Prestations/prestations.service';
import { CompletionProfileService } from '../../../../../../services/completionProfile/completion-profile.service';
import { PrestationResponse } from '../../../../../../models/responses/Prestations/prestation-response';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import { CompanyProfile } from '../../../../../../models/requests/CompanyProfile/CompanyProfile';
import { DevisResponse } from '../../../../../../models/responses/devis/devis-response';
import { GlobalResponse } from '../../../../../../models/responses/Auth/GlobalResponse';
import { environment } from '../../../../../../../environments/environment.development';
import { DevisRequest } from '../../../../../../models/requests/devis/devis-request';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from 'primeng/api';
import { catchError, of } from 'rxjs';
import Swal from 'sweetalert2';

declare var bootstrap: any;

@Component({
  selector: 'app-edit-devis',
  standalone: false,
  templateUrl: './edit-devis.component.html',
  styleUrls: ['./edit-devis.component.css'],
  providers: [MessageService, ConfirmationService]
})
export class EditDevisComponent implements OnInit {
  devisForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  clients: ClientResponse[] = [];
  filteredClients: ClientResponse[] = [];
  selectedClient: ClientResponse | null = null;
  
  // Ajouts pour les selects avec recherche
  clientFilterText: string = '';
  prestationFilterText: string = '';
  selectedPrestations: PrestationResponse[] = [];
  
  // Modal d'ajout de client
  clientForm: FormGroup;
  isClientFormLoading = false;
  clientErrorMessage = '';
  
  companyProfile: CompanyProfile | null = null;
  previewUrl: string = '';
  showPreview = false;
  prestations: PrestationResponse[] = [];
  filteredPrestations: PrestationResponse[] = [];
  prestationModal: any;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private devisService: DevisService,
    private clientService: ClientsService,
    private prestationService: PrestationsService,
    private companyProfileService: CompletionProfileService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {
    this.devisForm = this.fb.group({
      reference: ['', Validators.required],
      nomProjet: ['', Validators.required],
      description: [''],
      client: ['', Validators.required],
      statut: ['BROUILLON', Validators.required],
      dateEmission: ['', Validators.required],
      dateEcheance: ['', Validators.required],
      prestations: this.fb.array([]),
      tva: [0],
      remise: [0],
      notes: ['']
    });
    
    // Initialisation du formulaire client
    this.clientForm = this.fb.group({
      nom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', Validators.required],
      societe: ['', Validators.required],
      numeroTVA: ['', Validators.required],
      adresse: ['', Validators.required],
      ville: ['', Validators.required],
      pays: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadDevis();
    this.loadClients();
    this.loadCompanyProfile();
    this.loadPrestations();
    this.initModals();
  }

  private initModals(): void {
    setTimeout(() => {
      this.prestationModal = new bootstrap.Modal(
        document.getElementById('prestationModal')
      );
    }, 500);
  }

  private loadPrestations(): void {
    this.prestationService.findByCreatedBy().subscribe(
      (response) => {
        this.prestations = response || [];
        this.filteredPrestations = [...this.prestations];
        console.log('Prestations chargées:', this.prestations);
      },
      (error) => console.error('Erreur lors du chargement des prestations:', error)
    );
  }

  get prestationsArray() {
    return this.devisForm.get('prestations') as FormArray;
  }

  get totalHT(): number {
    return this.prestationsArray.controls.reduce((total, control) => {
      return total + (control.get('prixTotal')?.value || 0);
    }, 0);
  }

  get montantTVA(): number {
    return (this.totalHT * (this.devisForm.get('tva')?.value || 0)) / 100;
  }

  get montantRemise(): number {
    return (this.totalHT * (this.devisForm.get('remise')?.value || 0)) / 100;
  }

  get totalTTC(): number {
    return this.totalHT + this.montantTVA - this.montantRemise;
  }

  private loadDevis(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.errorMessage = 'ID du devis non trouvé';
      return;
    }

    this.isLoading = true;
    this.devisService.findByTrackingId(id).subscribe({
      next: (response: GlobalResponse<DevisResponse>) => {
        const devis = response.data;
        this.devisForm.patchValue({
          reference: devis.reference,
          nomProjet: devis.nomProjet,
          description: devis.description,
          client: devis.clientName,
          statut: devis.statut,
          dateEmission: devis.dateEmission,
          dateEcheance: devis.dateEcheance,
          tva: devis.tva,
          remise: devis.remise,
          notes: devis.notes
        });

        this.selectedClient = {
          trackingId: devis.clientTrackingId,
          nom: devis.clientName,
          email: devis.clientEmail
        } as ClientResponse;

        this.prestationsArray.clear();
        devis.prestations.forEach((prestation: PrestationResponse) => {
          this.prestationsArray.push(this.fb.group({
            trackingId: [prestation.trackingId],
            designation: [prestation.designation, Validators.required],
            description: [prestation.description],
            prixUnitaire: [prestation.prixUnitaire, [Validators.required, Validators.min(0)]],
            duree: [prestation.duree, [Validators.required, Validators.min(0.1)]],
            prixTotal: [prestation.prixTotal, Validators.required]
          }));
        });

        this.isLoading = false;
      },
      error: (error: any) => {
        this.errorMessage = 'Erreur lors du chargement du devis';
        this.isLoading = false;
        console.error('Erreur:', error);
      }
    });
  }

  private loadClients(): void {
    this.clientService.getClients().subscribe({
      next: (response: any) => {
        this.clients = response.data || response._embedded.clients;
        this.filteredClients = [...this.clients];
      },
      error: (error: any) => {
        console.error('Erreur lors du chargement des clients:', error);
      }
    });
  }

  private loadCompanyProfile(): void {
    this.companyProfileService.getProfile().subscribe({
      next: (profile: CompanyProfile) => {
        this.companyProfile = profile;
        if (profile.hasLogo) {
          this.previewUrl = `${environment.apiUrl}/api/profile/logo/${profile.trackingId}`;
        }
      },
      error: (error: any) => {
        console.error('Erreur lors du chargement du profil entreprise:', error);
      }
    });
  }

  // Méthode pour filtrer les clients dans le select
  filterClients(): ClientResponse[] {
    if (!this.clientFilterText) {
      return this.clients;
    }
    const filterValue = this.clientFilterText.toLowerCase();
    return this.clients.filter(client => 
      client.nom.toLowerCase().includes(filterValue) || 
      (client.societe && client.societe.toLowerCase().includes(filterValue)) ||
      (client.email && client.email.toLowerCase().includes(filterValue))
    );
  }
  
  // Méthode pour filtrer les prestations dans le select
  filterPrestations(): PrestationResponse[] {
    if (!this.prestationFilterText) {
      return this.prestations;
    }
    const filterValue = this.prestationFilterText.toLowerCase();
    return this.prestations.filter(prestation => 
      prestation.designation.toLowerCase().includes(filterValue) || 
      (prestation.description && prestation.description.toLowerCase().includes(filterValue))
    );
  }
  
  // Sélection d'un client dans le select
  onClientSelected(client: ClientResponse): void {
    this.selectedClient = client;
    this.devisForm.get('client')?.setValue(client.nom);
    this.devisForm.get('client')?.updateValueAndValidity();
    console.log('Client sélectionné et formulaire mis à jour:', client, this.devisForm.value);
  }
  
  // Sélection d'une prestation dans le multi-select
  onPrestationSelected(prestation: PrestationResponse): void {
    const id = prestation.trackingId || this.generateTrackingId();
    const prestationGroup = this.fb.group({
      trackingId: [id, Validators.required],
      designation: [prestation.designation, Validators.required],
      description: [prestation.description || ''],
      prixUnitaire: [prestation.prixUnitaire, [Validators.required, Validators.min(0)]],
      duree: [1, [Validators.required, Validators.min(0.1)]],
      prixTotal: [prestation.prixUnitaire, Validators.required]
    });
    this.prestationsArray.push(prestationGroup);
    this.updateTotals();
    console.log('Prestation sélectionnée ajoutée:', prestation, 'index:', this.prestationsArray.length - 1);
  }
  
  // Ouvrir le modal d'ajout de client
  openAddClientModal(): void {
    this.clientForm.reset();
    this.clientErrorMessage = '';
    this.isClientFormLoading = false;
    const addClientModal = new bootstrap.Modal(document.getElementById('addClientModal'));
    addClientModal.show();
  }
  
  // Soumettre le formulaire d'ajout de client
  onClientSubmit(): void {
    if (this.clientForm.invalid) {
      Object.keys(this.clientForm.controls).forEach(key => {
        this.clientForm.get(key)?.markAsTouched();
      });
      return;
    }
    
    this.isClientFormLoading = true;
    this.clientService.create(this.clientForm.value).subscribe({
      next: (response) => {
        this.isClientFormLoading = false;
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Client ajouté avec succès!'
        });
        // Fermer le modal
        const modalElement = document.getElementById('addClientModal');
        if (modalElement) {
          const modal = bootstrap.Modal.getInstance(modalElement);
          modal.hide();
        }
        // Recharger les clients et sélectionner le nouveau client
        this.loadClients();
        this.selectedClient = response;
        this.devisForm.get('client')?.setValue(response.nom);
      },
      error: (error) => {
        this.isClientFormLoading = false;
        this.clientErrorMessage = 'Erreur lors de l\'ajout du client: ' + (error.error?.message || 'Erreur inconnue');
        console.error('Erreur lors de l\'ajout du client:', error);
      }
    });
  }

  addEmptyPrestation(): void {
    const prestationGroup = this.fb.group({
      trackingId: [this.generateTrackingId(), Validators.required],
      designation: ['', Validators.required],
      description: [''],
      prixUnitaire: [0, [Validators.required, Validators.min(0)]],
      duree: [1, [Validators.required, Validators.min(0.1)]],
      prixTotal: [0, Validators.required]
    });
    this.prestationsArray.push(prestationGroup);
    console.log('Nouvelle prestation vide ajoutée:', prestationGroup.value);
    this.updateTotals();
  }

  removePrestation(index: number): void {
    this.prestationsArray.removeAt(index);
    console.log(
      'Suppression prestation index:', index,
      'prestations restantes:', this.prestationsArray.value
    );
    this.updateTotals();
  }

  updatePrestationTotal(index: number): void {
    const prestation = this.prestationsArray.at(index);
    const prixUnitaire = prestation.get('prixUnitaire')?.value || 0;
    const duree = prestation.get('duree')?.value || 0;
    const prixTotal = prixUnitaire * duree;
    prestation.get('prixTotal')?.setValue(prixTotal);
    console.log('Mise à jour du total pour la prestation:', {
      index,
      prixUnitaire,
      duree,
      prixTotal
    });
    this.updateTotals();
  }

  updateTotals(): void {
    // Les getters calculent automatiquement les totaux
    console.log('Mise à jour des totaux:', {
      totalHT: this.totalHT,
      montantTVA: this.montantTVA,
      montantRemise: this.montantRemise,
      totalTTC: this.totalTTC
    });
  }

  previewDevis(): void {
    this.showPreview = true;
  }

  onSubmit(): void {
    if (this.devisForm.invalid || !this.selectedClient) {
      this.errorMessage = 'Veuillez sélectionner un client';
      
      // Marquer tous les champs comme touchés pour afficher les erreurs
      Object.keys(this.devisForm.controls).forEach(key => {
        const control = this.devisForm.get(key);
        control?.markAsTouched();
      });
      
      // Vérifier si des prestations sont valides
      if (this.prestationsArray.length === 0) {
        this.errorMessage = 'Veuillez ajouter au moins une prestation';
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Veuillez ajouter au moins une prestation'
        });
        return;
      }
      
      // Vérifier la validité de chaque prestation
      let prestationsValides = true;
      this.prestationsArray.controls.forEach((control, index) => {
        if (control.invalid) {
          prestationsValides = false;
          // Convertir le contrôle en FormGroup pour accéder à ses propriétés
          const controlGroup = control as FormGroup;
          Object.keys(controlGroup.controls).forEach(field => {
            const fieldControl = controlGroup.get(field);
            fieldControl?.markAsTouched();
          });
          console.log('Prestation invalide:', control.value);
        }
      });
      
      if (!prestationsValides) {
        this.errorMessage = 'Certaines prestations sont invalides';
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Certaines prestations contiennent des erreurs'
        });
        return;
      }
      
      if (this.devisForm.invalid) {
        return;
      }
    }

    this.isLoading = true;
    
    // S'assurer que toutes les prestations ont des valeurs valides
    const prestationsFormatted = this.prestationsArray.controls.map(control => ({
      trackingId: control.get('trackingId')?.value || this.generateTrackingId(),
      designation: control.get('designation')?.value,
      description: control.get('description')?.value || '',
      prixUnitaire: control.get('prixUnitaire')?.value,
      duree: control.get('duree')?.value || 1,
      prixTotal: control.get('prixTotal')?.value || (control.get('prixUnitaire')?.value * (control.get('duree')?.value || 1))
    })).filter(p => p.designation && p.prixUnitaire > 0);
    
    if (prestationsFormatted.length === 0) {
      this.isLoading = false;
      this.errorMessage = 'Veuillez ajouter au moins une prestation valide';
      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: 'Veuillez ajouter au moins une prestation valide'
      });
      return;
    }

    const devisData: DevisRequest = {
      reference: this.devisForm.get('reference')?.value,
      nomProjet: this.devisForm.get('nomProjet')?.value,
      description: this.devisForm.get('description')?.value,
      client: this.selectedClient,
      dateEmission: this.devisForm.get('dateEmission')?.value,
      dateEcheance: this.devisForm.get('dateEcheance')?.value,
      prixTotal: this.totalHT,
      prixTTC: this.totalTTC,
      tva: this.devisForm.get('tva')?.value,
      remise: this.devisForm.get('remise')?.value,
      statut: this.devisForm.get('statut')?.value,
      notes: this.devisForm.get('notes')?.value,
      prestations: prestationsFormatted
    };

    console.log('Données envoyées:', JSON.stringify(devisData, null, 2));

    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.errorMessage = 'ID du devis non trouvé';
      this.isLoading = false;
      return;
    }

    this.devisService.update(id, devisData).subscribe({
      next: (response) => {
        console.log('Réponse du serveur:', response);
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Devis mis à jour avec succès!'
        }).then(() => {
          this.router.navigate(['/dashboard/devis']);
        });
      },
      error: (error: any) => {
        console.error('Erreur complète:', error);
        console.error('Détails de l\'erreur:', error.error);
        this.errorMessage = 'Erreur lors de la mise à jour du devis: ' + (error.error?.message || error.message);
        this.isLoading = false;
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: this.errorMessage
        });
      }
    });
  }

  deleteDevis(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.errorMessage = 'ID du devis non trouvé';
      return;
    }

    this.confirmationService.confirm({
      message: `Êtes-vous sûr de vouloir supprimer ce devis ?`,
      header: 'Confirmation de suppression',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Oui, supprimer',
      rejectLabel: 'Non, annuler',
      accept: () => {
        this.isLoading = true;
        this.devisService.deleteDevis(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Devis supprimé avec succès'
            });
            this.router.navigate(['/dashboard/devis']);
          },
          error: (error) => {
            this.isLoading = false;
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: `Erreur lors de la suppression du devis: ${error.error?.message || 'Erreur inconnue'}`
            });
            console.error('Erreur de suppression:', error);
          }
        });
      }
    });
  }
  
  private generateTrackingId(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      const r = Math.random() * 16 | 0;
      const v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }
}

