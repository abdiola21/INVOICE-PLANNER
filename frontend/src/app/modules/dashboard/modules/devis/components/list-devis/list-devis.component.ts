import { Component, OnInit } from '@angular/core';
import { DevisService } from '../../../../../../services/devis/devis.service';
import { FactureService } from '../../../../../../services/facture/facture.service';
import { EmailService } from '../../../../../../services/email/email.service';
import { DevisResponse } from '../../../../../../models/responses/devis/devis-response';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-devis',
  standalone: false,
  templateUrl: './list-devis.component.html',
  styleUrl: './list-devis.component.css'
})
export class ListDevisComponent implements OnInit {
  devis: DevisResponse[] = [];
  isLoading = false;
  errorMessage: string | null = null;
  activeFilter: string = 'tous';
  searchTerm: string = '';
  filteredDevis: DevisResponse[] = [];
  selectedDevis: DevisResponse | null = null;

  constructor(
    private devisService: DevisService,
    private factureService: FactureService,
    private emailService: EmailService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDevis();
  }

  loadDevis(): void {
    this.isLoading = true;
    this.devisService.findByCreatedBy().subscribe({
      next: (response) => {
        this.devis = response.data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des devis';
        this.isLoading = false;
        console.error('Erreur:', error);
      }
    });
  }

  applyFilters(): void {
    // D'abord filtrer par statut
    let result = this.devis;

    if (this.activeFilter !== 'tous') {
      result = this.devis.filter(d => d.statut === this.activeFilter);
    }

    // Ensuite appliquer la recherche textuelle
    if (this.searchTerm && this.searchTerm.trim() !== '') {
      const search = this.searchTerm.toLowerCase().trim();
      result = result.filter(d =>
        d.nomProjet.toLowerCase().includes(search) ||
        d.reference.toLowerCase().includes(search) ||
        (d.clientName && d.clientName.toLowerCase().includes(search))
      );
    }

    this.filteredDevis = result;
  }

  filterDevis(status: string): void {
    this.activeFilter = status;
    this.applyFilters();
  }

  convertToFacture(devis: DevisResponse): void {
    if (devis.statut !== 'ACCEPTE') {
      Swal.fire({
        icon: 'warning',
        title: 'Attention',
        text: 'Seuls les devis acceptés peuvent être convertis en facture'
      });
      return;
    }

    Swal.fire({
      title: 'Confirmation',
      text: `Êtes-vous sûr de vouloir convertir le devis ${devis.reference} en facture ? Un email sera envoyé au client avant la conversion.`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Oui, convertir',
      cancelButtonText: 'Non, annuler',
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33'
    }).then((result) => {
      if (result.isConfirmed) {
        // Envoi du devis par email avant conversion
        this.isLoading = true;
        this.emailService.sendDevisByEmail(devis.trackingId).subscribe({
          next: (emailResponse) => {
            // Après l'envoi réussi de l'email, procéder à la conversion
            this.factureService.createFromDevis(devis.trackingId).subscribe({
              next: (response) => {
                this.isLoading = false;
                Swal.fire({
                  icon: 'success',
                  title: 'Succès',
                  text: 'Devis envoyé par email et converti en facture avec succès',
                  timer: 2000,
                  showConfirmButton: false
                }).then(() => {
                  this.router.navigate(['/dashboard/factures']);
                });
              },
              error: (error) => {
                this.isLoading = false;
                Swal.fire({
                  icon: 'error',
                  title: 'Erreur',
                  text: 'Erreur lors de la conversion en facture'
                });
                console.error('Erreur de conversion:', error);
              }
            });
          },
          error: (error) => {
            this.isLoading = false;
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Erreur lors de l\'envoi du devis par email'
            });
            console.error('Erreur d\'envoi d\'email:', error);
          }
        });
      }
    });
  }

  // Nouvelle méthode pour envoyer uniquement le devis par email
  sendDevisByEmail(devis: DevisResponse): void {
    Swal.fire({
      title: 'Confirmation',
      text: `Voulez-vous envoyer le devis ${devis.reference} par email au client ?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Oui, envoyer',
      cancelButtonText: 'Non, annuler',
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.emailService.sendDevisByEmail(devis.trackingId).subscribe({
          next: (response) => {
            this.isLoading = false;
            Swal.fire({
              icon: 'success',
              title: 'Succès',
              text: 'Devis envoyé par email avec succès',
              timer: 2000,
              showConfirmButton: false
            });
          },
          error: (error) => {
            this.isLoading = false;
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Erreur lors de l\'envoi du devis par email'
            });
            console.error('Erreur d\'envoi d\'email:', error);
          }
        });
      }
    });
  }

  updateDevisStatus(devis: DevisResponse): void {
    const oldStatus = devis.statut;
    this.devisService.updateDevisStatus(devis.trackingId, devis.statut).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: `Statut du devis ${devis.reference} mis à jour avec succès`,
          timer: 2000,
          showConfirmButton: false
        });
        this.loadDevis();
      },
      error: (error) => {
        devis.statut = oldStatus; // Restaurer l'ancien statut en cas d'erreur
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: `Erreur lors de la mise à jour du statut: ${error.error?.message || 'Erreur inconnue'}`
        });
        console.error('Erreur de mise à jour du statut:', error);
      }
    });
  }

  downloadDevis(devis: DevisResponse): void {
    this.devisService.downloadDevisPDF(devis.trackingId).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Devis_${devis.reference}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Erreur lors du téléchargement du PDF'
        });
        console.error('Erreur de téléchargement:', error);
      }
    });
  }

  deleteDevis(devis: DevisResponse): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: `Voulez-vous vraiment supprimer le devis ${devis.reference} ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Non, annuler',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;
        this.devisService.deleteDevis(devis.trackingId).subscribe({
          next: (response) => {
            Swal.fire({
              icon: 'success',
              title: 'Supprimé !',
              text: `Le devis ${devis.reference} a été supprimé avec succès`,
              timer: 2000,
              showConfirmButton: false
            });
            this.loadDevis();
          },
          error: (error) => {
            this.isLoading = false;
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: `Erreur lors de la suppression du devis: ${error.error?.message || 'Erreur inconnue'}`
            });
            console.error('Erreur de suppression:', error);
          }
        });
      }
    });
  }

  openDetailsModal(devis: DevisResponse): void {
    this.selectedDevis = devis;
  }

  addDevis(): void {
    this.router.navigate(['add-devis']);
  }

  editDevis(id: string): void {
    this.router.navigate(['edit-devis', id]);
  }
}

