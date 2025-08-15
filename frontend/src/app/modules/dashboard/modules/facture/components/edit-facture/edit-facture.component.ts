import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FactureService } from '../../../../../../services/facture/facture.service';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { FactureResponse } from '../../../../../../models/responses/facture/facture-response';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import Swal from 'sweetalert2';

declare var bootstrap: any;

@Component({
  selector: 'app-edit-facture',
  standalone: false,
  templateUrl: './edit-facture.component.html',
  styleUrl: './edit-facture.component.css'
})
export class EditFactureComponent implements OnInit {
  factureForm: FormGroup;
  factureId: string;
  facture: FactureResponse | null = null;
  isLoading = false;
  errorMessage: string | null = null;
  clients: ClientResponse[] = [];
  filteredClients: ClientResponse[] = [];
  selectedClient: ClientResponse | null = null;
  searchTermClient = '';
  clientModal: any;
  
  constructor(
    private factureService: FactureService,
    private clientsService: ClientsService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.factureId = this.route.snapshot.paramMap.get('id') || '';
    this.factureForm = this.fb.group({
      numero: ['', Validators.required],
      clientName: ['', Validators.required],
      clientTrackingId: ['', Validators.required],
      dateEcheance: ['', Validators.required],
      montantHT: [0, [Validators.required, Validators.min(0)]],
      remise: [0, [Validators.min(0)]],
      montantTTC: [0, [Validators.required, Validators.min(0)]],
      etat: ['EN_ATTENTE', Validators.required],
      modeReglement: ['', Validators.required],
      referenceDevis: ['']
    });
  }

  ngOnInit(): void {
    this.loadClients();
    this.initModals();
    
    if (this.factureId) {
      this.loadFacture();
    } else {
      this.router.navigate(['/dashboard/factures']);
    }
  }

  private initModals(): void {
    setTimeout(() => {
      this.clientModal = new bootstrap.Modal(
        document.getElementById('clientModal')
      );
    }, 500);
  }

  private loadClients(): void {
    this.clientsService.getClients().subscribe(
      (response) => {
        this.clients = response || [];
        this.filteredClients = [...this.clients];
      },
      (error) => {
        console.error('Erreur lors du chargement des clients:', error);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de charger la liste des clients'
        });
      }
    );
  }

  loadFacture(): void {
    this.isLoading = true;
    this.factureService.findByTrackingId(this.factureId).subscribe({
      next: (response) => {
        this.facture = response.data;
        this.populateForm();
        
        // Trouver le client correspondant
        if (this.facture && this.facture.clientTrackingId) {
          const client = this.clients.find(c => c.trackingId === this.facture?.clientTrackingId);
          if (client) {
            this.selectedClient = client;
          } else {
            // Si le client n'est pas trouvé dans la liste, créer un objet client avec les infos disponibles
            this.selectedClient = {
              trackingId: this.facture.clientTrackingId,
              nom: this.facture.clientName,
              adresse: '',
              email: '',
              telephone: '',
              societe: '',
              numeroTVA: '',
              ville: '',
              pays: ''
            };
          }
        }
        
        this.isLoading = false;
      },
      error: (error: any) => {
        this.errorMessage = 'Erreur lors du chargement de la facture';
        this.isLoading = false;
        console.error('Erreur:', error);
      }
    });
  }

  populateForm(): void {
    if (this.facture) {
      this.factureForm.patchValue({
        numero: this.facture.numero,
        clientName: this.facture.clientName,
        clientTrackingId: this.facture.clientTrackingId,
        dateEcheance: this.formatDate(this.facture.dateEcheance),
        montantHT: this.facture.montantHT,
        remise: this.facture.remise,
        montantTTC: this.facture.montantTTC,
        etat: this.facture.etat,
        modeReglement: this.facture.modeReglement,
        referenceDevis: this.facture.referenceDevis
      });
    }
  }

  openClientModal(): void {
    this.searchTermClient = '';
    this.filteredClients = [...this.clients];
    this.clientModal.show();
  }

  filterClients(): void {
    const term = this.searchTermClient.toLowerCase();
    this.filteredClients = this.clients.filter((client) =>
      client.nom.toLowerCase().includes(term) ||
      client.email.toLowerCase().includes(term) ||
      (client.societe && client.societe.toLowerCase().includes(term))
    );
  }

  selectClient(client: ClientResponse): void {
    this.selectedClient = client;
    this.factureForm.patchValue({
      clientName: client.nom,
      clientTrackingId: client.trackingId
    });
    this.clientModal.hide();
  }

  formatDate(date: Date): string {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  onSubmit(): void {
    if (this.factureForm.invalid || !this.selectedClient) {
      if (!this.selectedClient) {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Veuillez sélectionner un client'
        });
      }
      return;
    }

    this.isLoading = true;
    const updatedFacture = {
      ...this.factureForm.value,
      trackingId: this.factureId
    };

    this.factureService.updateFacture(updatedFacture).subscribe({
      next: (response: any) => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Facture mise à jour avec succès',
          timer: 2000,
          showConfirmButton: false
        }).then(() => {
          this.router.navigate(['/dashboard/factures']);
        });
      },
      error: (error: any) => {
        this.isLoading = false;
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: `Erreur lors de la mise à jour de la facture: ${error.error?.message || 'Erreur inconnue'}`
        });
        console.error('Erreur de mise à jour:', error);
      }
    });
  }

  calculateTTC(): void {
    const montantHT = this.factureForm.get('montantHT')?.value || 0;
    const remise = this.factureForm.get('remise')?.value || 0;
    const montantTTC = montantHT - remise;
    this.factureForm.patchValue({ montantTTC });
  }

  goBack(): void {
    this.router.navigate(['/dashboard/factures']);
  }
}

