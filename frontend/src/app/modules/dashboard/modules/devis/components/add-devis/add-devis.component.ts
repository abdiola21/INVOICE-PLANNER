import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DevisRequest } from '../../../../../../models/requests/devis/devis-request';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import { PrestationResponse } from '../../../../../../models/responses/Prestations/prestation-response';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { DevisService } from '../../../../../../services/devis/devis.service';
import { PrestationsService } from '../../../../../../services/Prestations/prestations.service';
import { catchError, finalize, of } from 'rxjs';
import { CompanyProfile } from '../../../../../../models/requests/CompanyProfile/CompanyProfile';
import { CompletionProfileService } from '../../../../../../services/completionProfile/completion-profile.service';

// Ajout de l'import SweetAlert2
import Swal from 'sweetalert2';

declare var bootstrap: any;

@Component({
  selector: 'app-add-devis',
  templateUrl: './add-devis.component.html',
  styleUrls: ['./add-devis.component.css'],
  standalone: false,
})
export class AddDevisComponent implements OnInit {
[x: string]: any;
  devisForm: FormGroup;
  clients: ClientResponse[] = [];
  filteredClients: ClientResponse[] = [];
  prestations: PrestationResponse[] = [];
  filteredPrestations: PrestationResponse[] = [];
  selectedClient: ClientResponse | null = null;
  companyProfile: CompanyProfile | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  today: string = new Date().toISOString().split('T')[0];
  todayPlusOneMonth: string = (() => {
    const date = new Date();
    date.setMonth(date.getMonth() + 1);
    return date.toISOString().split('T')[0];
  })();

  loading = false;
  showPreview = false;
  searchTermClient = '';
  searchTermPrestation = '';

  // Ajouts pour les selects avec recherche
  clientFilterText: string = '';
  prestationFilterText: string = '';
  selectedPrestations: PrestationResponse[] = [];
  
  // Modal d'ajout de client
  clientForm: FormGroup;
  isClientFormLoading = false;
  clientErrorMessage = '';
  clientModal: any;

  // Totaux calculés
  totalHT = 0;
  montantTVA = 0;
  montantRemise = 0;
  totalTTC = 0;

  // Modals
  prestationModal: any;
  previewModal: any;

  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private devisService: DevisService,
    private clientsService: ClientsService,
    private prestationsService: PrestationsService,
    private companyProfileService: CompletionProfileService,
    private router: Router
  ) {
    this.devisForm = this.fb.group({
      reference: ['', Validators.required],
      nomProjet: ['', Validators.required],
      description: [''],
      client: [null, Validators.required],
      dateEmission: [new Date(), Validators.required],
      dateEcheance: [this.addDays(new Date(), 30), Validators.required],
      prixTotal: [0, Validators.required],
      prixTTC: [0, Validators.required],
      tva: [20, Validators.required],
      remise: [0, Validators.required],
      statut: ['BROUILLON', Validators.required],
      notes: [''],
      prestations: this.fb.array([])
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
    
    console.log('Formulaire initialisé:', this.devisForm.value);
  }

  ngOnInit(): void {
    console.log('ngOnInit AddDevisComponent');
    this.loadClients();
    this.loadPrestations();
    this.loadCompanyProfile();
    this.initModals();
    console.log('Formulaire après init (avant référence):', this.devisForm.value);

    const today = new Date();
    const generatedRef = `DEV-${today.getFullYear()}${(today.getMonth() + 1)
      .toString()
      .padStart(2, '0')}-${this.generateRandomReference()}`;
    console.log('Référence générée:', generatedRef);
    this.devisForm.patchValue({ reference: generatedRef });
  }

  private loadCompanyProfile(): void {
    console.log('Chargement du profil...');
    this.loading = true;
    this.companyProfileService
      .getProfile()
      .pipe(
        catchError((err) => {
          console.error('Erreur lors du chargement du profil:', err);
          if (err.status !== 404) {
            this.errorMessage = 'Erreur lors du chargement du profil';
          }
          return of(null);
        }),
        finalize(() => {
          this.loading = false;
          console.log('Fin du chargement du profil');
        })
      )
      .subscribe((profile) => {
        console.log('Profil récupéré:', profile);
        if (profile) {
          this.companyProfile = profile;
          if (profile.trackingId && profile.hasLogo) {
            console.log("Le profil a un logo, mise à jour de l'URL de prévisualisation");
            this.updatePreviewUrl(profile.trackingId);
          } else {
            console.log("Le profil n'a pas de logo ou pas de trackingId:", profile);
          }
        }
      });
  }

  private updatePreviewUrl(profileId: string): void {
    this.companyProfileService
      .getLogoImage(profileId)
      .pipe(
        catchError((err) => {
          console.error('Erreur lors du chargement du logo:', err);
          return of(null);
        })
      )
      .subscribe((blob) => {
        if (blob) {
          this.previewUrl = URL.createObjectURL(blob);
          console.log('Preview logo URL (cache bust):', this.previewUrl);
        }
      });
  }

  private initModals(): void {
    setTimeout(() => {
      this.clientModal = new bootstrap.Modal(
        document.getElementById('clientModal')
      );
      this.prestationModal = new bootstrap.Modal(
        document.getElementById('prestationModal')
      );
      this.previewModal = new bootstrap.Modal(
        document.getElementById('previewModal')
      );
      console.log('Modals initialisés:', {
        clientModal: this.clientModal,
        prestationModal: this.prestationModal,
        previewModal: this.previewModal
      });
    }, 500);
  }

  private loadClients(): void {
    this.clientsService.getClients().subscribe(
      (response) => {
        this.clients = response || [];
        this.filteredClients = [...this.clients];
        console.log('Clients chargés:', this.clients);
      },
      (error) =>
        console.error('Erreur lors du chargement des clients:', error)
    );
  }

  private loadPrestations(): void {
    this.prestationsService.findByCreatedBy().subscribe(
      (response) => {
        this.prestations = response || [];
        this.filteredPrestations = [...this.prestations];
        console.log('Prestations chargées:', this.prestations);
      },
      (error) =>
        console.error('Erreur lors du chargement des prestations:', error)
    );
  }

  get prestationsArray(): FormArray {
    const arr = this.devisForm.get('prestations') as FormArray;
    console.log('Accès au FormArray des prestations:', arr.value);
    return arr;
  }

  addEmptyPrestation(): void {
    const prestationGroup = this.fb.group({
      designation: ['', Validators.required],
      description: [''],
      prixUnitaire: [0, [Validators.required, Validators.min(0)]],
      duree: [1, [Validators.required, Validators.min(0.1)]],
      prixTotal: [0, Validators.required]
    });
    this.prestationsArray.push(prestationGroup);
    console.log("Ajout d'une prestation vide, index:",
      this.prestationsArray.length - 1,
      this.prestationsArray.value);
    
    // Mettre à jour immédiatement les totaux
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
    const prestationGroup = this.prestationsArray.at(index) as FormGroup;
    const prixUnitaire = prestationGroup.get('prixUnitaire')?.value || 0;
    const duree = prestationGroup.get('duree')?.value || 0;
    const total = prixUnitaire * duree;

    prestationGroup.patchValue({ prixTotal: total });
    console.log(
      `Mise à jour prestation ${index}, prixUnitaire: ${prixUnitaire},`,
      `durée: ${duree}, total: ${total}`
    );
    this.updateTotals();
  }

  updateTotals(): void {
    this.totalHT = this.prestationsArray.controls.reduce(
      (sum, control) => sum + (control.get('prixTotal')?.value || 0),
      0
    );

    const tvaRate = this.devisForm.get('tva')?.value || 0;
    this.montantTVA = (this.totalHT * tvaRate) / 100;

    const remiseRate = this.devisForm.get('remise')?.value || 0;
    this.montantRemise = (this.totalHT * remiseRate) / 100;

    this.totalTTC = this.totalHT + this.montantTVA - this.montantRemise;

    console.log(
      `Totaux mis à jour - HT: ${this.totalHT}, TVA: ${this.montantTVA},`,
      `Remise: ${this.montantRemise}, TTC: ${this.totalTTC}`
    );
    this.devisForm.patchValue({
      prixTotal: this.totalHT,
      prixTTC: this.totalTTC
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
    this.devisForm.get('client')?.setValue(client);
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
    this.clientsService.create(this.clientForm.value).subscribe({
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
        this.devisForm.get('client')?.setValue(response);
      },
      error: (error) => {
        this.isClientFormLoading = false;
        this.clientErrorMessage = 'Erreur lors de l\'ajout du client: ' + (error.error?.message || 'Erreur inconnue');
        console.error('Erreur lors de l\'ajout du client:', error);
      }
    });
  }

  previewDevis(): void {
    this.showPreview = true;
    console.log('Aperçu du devis généré:', this.devisForm.value);
    this.previewModal.show();
  }

  exportToPDF(): void {
    console.log('Export PDF demandé pour le devis:', this.devisForm.value);
    // Remplacement de alert() par SweetAlert2
    Swal.fire({
      icon: 'info',
      title: 'Fonctionnalité à venir',
      text: "Fonctionnalité d'export en PDF à implémenter",
      confirmButtonText: 'OK'
    });
  }

  onSubmit(): void {
    if (this.devisForm.invalid || !this.selectedClient) {
      if (!this.selectedClient) {
        this.devisForm.get('client')?.markAsTouched();
        this.errorMessage = 'Veuillez sélectionner un client';
      }
      
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

    this.loading = true;
    const formValue = this.devisForm.value;
    
    // S'assurer que toutes les prestations ont des valeurs valides
    const prestationsFormatted = formValue.prestations
      .filter((p: any) => p.designation && p.prixUnitaire > 0)
      .map((p: any) => ({
        trackingId: p.trackingId || this.generateTrackingId(),
        designation: p.designation,
        description: p.description || '',
        prixUnitaire: p.prixUnitaire,
        duree: p.duree || 1,
        prixTotal: p.prixTotal || (p.prixUnitaire * (p.duree || 1))
      }));
    
    if (prestationsFormatted.length === 0) {
      this.loading = false;
      this.errorMessage = 'Veuillez ajouter au moins une prestation valide';
      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: 'Veuillez ajouter au moins une prestation valide'
      });
      return;
    }

    const devisRequest: DevisRequest = {
      reference: formValue.reference,
      nomProjet: formValue.nomProjet,
      description: formValue.description,
      client: this.selectedClient,
      dateEmission: formValue.dateEmission,
      dateEcheance: formValue.dateEcheance,
      prixTotal: this.totalHT,
      prixTTC: this.totalTTC,
      tva: formValue.tva,
      remise: formValue.remise,
      statut: formValue.statut,
      notes: formValue.notes,
      prestations: prestationsFormatted
    };

    console.log('Requête de création du devis avec prestations:', devisRequest);

    this.devisService.create(devisRequest).subscribe(
      response => {
        this.loading = false;
        console.log('Devis créé avec succès:', response);
        // Remplacement de alert() par SweetAlert2
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Devis créé avec succès!',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['/dashboard/devis']);
        });
      },
      error => {
        this.loading = false;
        console.error('Erreur lors de la création du devis:', error);
        // Remplacement de alert() par SweetAlert2
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Une erreur est survenue lors de la création du devis: ' + (error.error?.message || 'Erreur inconnue'),
          confirmButtonText: 'OK'
        });
      }
    );
  }

  private addDays(date: Date, days: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    console.log(
      `addDays: date initiale ${date}, jours ajoutés ${days}, résultat ${result}`
    );
    return result;
  }

  private generateRandomReference(): string {
    const ref = Math.floor(100 + Math.random() * 900).toString();
    console.log('generateRandomReference:', ref);
    return ref;
  }

  generateTrackingId(): string {
    return Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
  }
}

