import { Component, OnInit } from '@angular/core';
import { FactureService } from '../../../../../../services/facture/facture.service';
import { FactureResponse, PrestationFacture } from '../../../../../../models/responses/facture/facture-response';
import { Router } from '@angular/router';
import { CompletionProfileService } from '../../../../../../services/completionProfile/completion-profile.service';
import { CompanyProfile } from '../../../../../../models/requests/CompanyProfile/CompanyProfile';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import { catchError, finalize, of } from 'rxjs';
import Swal from 'sweetalert2';
import html2pdf from 'html2pdf.js';

declare var bootstrap: any;

@Component({
  selector: 'app-list-facture',
  standalone: false,
  templateUrl: './list-facture.component.html',
  styleUrl: './list-facture.component.css'
})
export class ListFactureComponent implements OnInit {
  factures: FactureResponse[] = [];
  clients: ClientResponse[] = [];
  isLoading = false;
  errorMessage: string | null = null;
  activeFilter: string = 'tous';
  searchTerm: string = '';
  filteredFactures: FactureResponse[] = [];
  selectedFacture: FactureResponse | null = null;
  companyProfile: CompanyProfile | null = null;
  previewUrl: string | ArrayBuffer | null = null;

  // Ajout d'une propriété pour stocker le client lié à la facture sélectionnée
  selectedClient: ClientResponse | null = null;

  constructor(
    private factureService: FactureService,
    private companyProfileService: CompletionProfileService,
    private clientService: ClientsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFactures();
    this.loadCompanyProfile();
    // this.loadClients();
  }

  private loadCompanyProfile(): void {
    this.companyProfileService
      .getProfile()
      .pipe(
        catchError((err) => {
          console.error('Erreur lors du chargement du profil:', err);
          return of(null);
        })
      )
      .subscribe((profile) => {
        if (profile) {
          this.companyProfile = profile;
          if (profile.trackingId && profile.hasLogo) {
            this.updatePreviewUrl(profile.trackingId);
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
        }
      });
  }

  loadFactures(): void {
    this.isLoading = true;
    this.factureService.findAll().subscribe({
      next: (response) => {
        // Trier les factures du plus récent au moins récent selon la date d'émission ou de création
        this.factures = (response.data || []).sort((a: any, b: any) => {
          const dateA = new Date(a.dateEmission || a.dateFacture || a.createdAt || 0);
          const dateB = new Date(b.dateEmission || b.dateFacture || b.createdAt || 0);
          return dateB.getTime() - dateA.getTime();
        });
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des factures';
        this.isLoading = false;
        console.error('Erreur:', error);
      }
    });
  }

  applyFilters(): void {
    let result = this.factures;

    if (this.activeFilter !== 'tous') {
      result = this.factures.filter(f => f.etat === this.activeFilter);
    }

    if (this.searchTerm && this.searchTerm.trim() !== '') {
      const search = this.searchTerm.toLowerCase().trim();
      result = result.filter(f =>
        f.numero.toLowerCase().includes(search) ||
        f.clientName.toLowerCase().includes(search) ||
        f.referenceDevis.toLowerCase().includes(search)
      );
    }

    this.filteredFactures = result;
  }

  filterFactures(status: string): void {
    this.activeFilter = status;
    this.applyFilters();
  }

  downloadFacture(facture: FactureResponse): void {
    // TODO: Implémenter le téléchargement de la facture
    Swal.fire({
      icon: 'info',
      title: 'Information',
      text: 'La fonctionnalité de téléchargement sera bientôt disponible'
    });
  }

  deleteFacture(facture: FactureResponse): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: `Voulez-vous vraiment supprimer la facture ${facture.numero} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Non, annuler',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6'
    }).then((result) => {
      if (result.isConfirmed) {
        // TODO: Implémenter la suppression de la facture
        Swal.fire({
          icon: 'info',
          title: 'Information',
          text: 'La fonctionnalité de suppression sera bientôt disponible'
        });
      }
    });
  }

  /**
   * Ouvre la modale de détails de la facture et récupère l'objet client lié à la facture via le ClientService.
   */
  openDetailsModal(facture: FactureResponse): void {
    this.selectedFacture = facture;
    this.selectedClient = null; // Réinitialiser le client sélectionné

    // On suppose que la facture possède un champ clientTrackingId ou similaire pour identifier le client
    // Si ce champ s'appelle différemment, il faut l'adapter ici
    const clientTrackingId = facture.clientTrackingId;

    if (clientTrackingId) {
      this.clientService.getClientById(clientTrackingId)
        .pipe(
          catchError((err) => {
            console.error('Erreur lors de la récupération du client lié à la facture:', err);
            return of(null);
          })
        )
        .subscribe((response) => {
          if (response && response) {
            this.selectedClient = response;
            console.log('Client lié à la facture:', this.selectedClient);
          } else {
            this.selectedClient = null;
          }
        });
    } else {
      this.selectedClient = null;
    }

    // Ouvre la modale Bootstrap
    const modalElement = document.getElementById('factureDetailsModal');
    if (modalElement) {
      // @ts-ignore: Bootstrap's Modal is not fully typed by default
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  editFacture(id: string): void {
    this.router.navigate(['/dashboard/factures/edit-facture', id]);
  }

  
  
  exportToPDF(): void {
    if (!this.selectedFacture) return;
    
    // Formater les valeurs numériques avant l'export
    try {
      // S'assurer que les valeurs numériques sont correctement formatées
      this.formatFactureValues();
      
      const element = document.getElementById('facture-preview-pdf');
      if (!element) return;
      
      // Modifier les options pour avoir un PDF plus professionnel
      const opt = {
        margin: [15, 15, 15, 15], // Marges en mm [haut, droite, bas, gauche]
        filename: `Facture_${this.selectedFacture.numero}.pdf`,
        image: { type: 'jpeg', quality: 1.0 },
        html2canvas: { 
          scale: 2,
          useCORS: true,
          logging: false
        },
        jsPDF: { 
          unit: 'mm', 
          format: 'a4', 
          orientation: 'portrait',
          compress: true
        },
        pagebreak: { mode: 'avoid-all' }
      };

      // Notification pendant la génération
      Swal.fire({
        title: 'Génération du PDF...',
        html: 'Veuillez patienter pendant la génération du PDF.',
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
          // Générer le PDF
          html2pdf().set(opt).from(element).save().then(() => {
            Swal.close();
          }).catch((error: Error) => {
            console.error('Erreur lors de la génération du PDF:', error);
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Une erreur est survenue lors de la génération du PDF.'
            });
          });
        }
      });
    } catch (error) {
      console.error('Erreur lors de la préparation du PDF:', error);
      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: 'Une erreur est survenue lors de la préparation du PDF.'
      });
    }
  }
  
  /**
   * Formater les valeurs numériques pour l'affichage
   * Cette méthode s'assure que les valeurs numériques sont correctement formatées
   * avant l'export en PDF
   */
  private formatFactureValues(): void {
    if (!this.selectedFacture) return;
    
    // Formater montantHT et montantTTC si ce sont des nombres
    if (typeof this.selectedFacture.montantHT === 'number') {
      this.selectedFacture.montantHT = parseFloat(this.selectedFacture.montantHT.toFixed(2));
    }
    
    if (typeof this.selectedFacture.montantTTC === 'number') {
      this.selectedFacture.montantTTC = parseFloat(this.selectedFacture.montantTTC.toFixed(2));
    }
    
    // Formater les prestations si elles existent
    if (this.selectedFacture.prestations && this.selectedFacture.prestations.length > 0) {
      this.selectedFacture.prestations.forEach((prestation: PrestationFacture) => {
        if (typeof prestation.prixUnitaire === 'number') {
          prestation.prixUnitaire = parseFloat(prestation.prixUnitaire.toFixed(2));
        }
        if (typeof prestation.prixTotal === 'number') {
          prestation.prixTotal = parseFloat(prestation.prixTotal.toFixed(2));
        }
      });
    }
    
    // Formater la remise si elle existe
    if (typeof this.selectedFacture.remise === 'number') {
      this.selectedFacture.remise = parseFloat(this.selectedFacture.remise.toFixed(2));
    }
  }

  updateFactureStatus(facture: FactureResponse): void {
    const oldStatus = facture.etat;
    this.factureService.updateStatus(facture.trackingId, facture.etat).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: `Statut de la facture ${facture.numero} mis à jour avec succès`,
          timer: 2000,
          showConfirmButton: false
        });
        this.loadFactures();
      },
      error: (error) => {
        facture.etat = oldStatus; // Restaurer l'ancien statut en cas d'erreur
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: `Erreur lors de la mise à jour du statut: ${error.error?.message || 'Erreur inconnue'}`
        });
        console.error('Erreur de mise à jour du statut:', error);
      }
    });
  }
}



