import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FactureService } from '../../../../../../services/facture/facture.service';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import Swal from 'sweetalert2';
declare var bootstrap: any;

@Component({
  selector: 'app-add-facture',
  standalone: false,
  templateUrl: './add-facture.component.html',
  styleUrl: './add-facture.component.css'
})
export class AddFactureComponent implements OnInit {
  factureForm: FormGroup;
  isLoading = false;
  clients: ClientResponse[] = [];
  filteredClients: ClientResponse[] = [];
  selectedClient: ClientResponse | null = null;
  searchTermClient = '';
  clientModal: any;

  constructor(
    private factureService: FactureService,
    private clientsService: ClientsService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.factureForm = this.fb.group({
      clientName: ['', Validators.required],
      clientTrackingId: ['', Validators.required],
      dateEcheance: ['', Validators.required],
      montantHT: [0, [Validators.required, Validators.min(0)]],
      remise: [0, [Validators.min(0)]],
      montantTTC: [0, [Validators.required, Validators.min(0)]],
      etat: ['BROUILLON', Validators.required],
      modeReglement: ['', Validators.required],
      referenceDevis: ['']
    });
  }

  ngOnInit(): void {
    this.loadClients();
    this.initModals();
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

  openClientModal(): void {
    this.searchTermClient = '';
    this.filteredClients = [...this.clients];
    this.clientModal.show();
  }

  filterClients(): void {
  const term = (this.searchTermClient || '').toLowerCase();
  this.filteredClients = this.clients.filter((client) => {
      const nom = (client.nom || '').toLowerCase();
      const email = (client.email || '').toLowerCase();
      const societe = (client.societe || '').toLowerCase();
      return nom.includes(term) || email.includes(term) || societe.includes(term);
    });
  }


  selectClient(client: ClientResponse): void {
    this.selectedClient = client;
    this.factureForm.patchValue({
      clientName: client.nom,
      clientTrackingId: client.trackingId
    });
    this.clientModal.hide();
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
    const newFacture = this.prepareFactureData();

    this.factureService.createFacture(newFacture).subscribe({
      next: (response: any) => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Facture créée avec succès',
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
          text: `Erreur lors de la création de la facture: ${error.error?.message || 'Erreur inconnue'}`
        });
        console.error('Erreur de création:', error);
      }
    });
  }

  prepareFactureData() {
    const factureData = this.factureForm.value;
    const formattedDate = this.formatDate(factureData.dateEcheance);
    
    return {
      clientId: this.selectedClient?.id,
      referenceDevis: factureData.referenceDevis,
      dateEcheance: formattedDate,
      modeReglement: factureData.modeReglement,
      etat: factureData.etat,
      remise: factureData.remise,
      montantHT: factureData.montantHT,
      montantTTC: factureData.montantTTC,
      lignes: [
        {
          designation: 'Prestation',  // Remplacer avec des données réelles si disponibles
          prixUnitaire: factureData.montantHT, 
          quantite: 1  // Remplacer selon la logique de la facture
        }
      ]
    };
  }

  formatDate(date: string): string {
    const newDate = new Date(date);
    return newDate.toISOString().split('T')[0]; // Retourne la date au format 'YYYY-MM-DD'
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

