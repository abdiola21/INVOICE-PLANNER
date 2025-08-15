import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import Swal from 'sweetalert2';
import { PrestationResponse } from '../../../../../../models/responses/Prestations/prestation-response';
import { PrestationsService } from '../../../../../../services/Prestations/prestations.service';

@Component({
  selector: 'app-list-prestation',
  standalone: false,
  templateUrl: './list-prestation.component.html',
  styleUrl: './list-prestation.component.css',
})
export class ListPrestationComponent {
  PrestationsResponse: PrestationResponse[] = [];
  loading: boolean = true;
  error: string | null = null;
  searchTerm: string = '';

  constructor(
    private PrestationsService: PrestationsService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.getPrestations();
  }

  getPrestations(): void {
    this.loading = true;
    this.error = null;
    
    this.PrestationsService.findByCreatedBy().subscribe(
      (response) => {
        this.loading = false;
        if (response) {
          console.log(response);
          this.PrestationsResponse = response;
        } else {
          console.log(response);
          this.PrestationsResponse = [];
        }
      },
      (error) => {
        this.loading = false;
        this.error = error.message || 'Une erreur est survenue lors du chargement des prestations';
        console.error('Erreur lors du chargement des prestations:', error);
      }
    );
  }

  refreshData(): void {
    this.getPrestations();
    this.messageService.add({
      severity: 'info',
      summary: 'Actualisation',
      detail: 'Liste des prestations actualisée',
      life: 3000
    });
  }

  addPrestation(): void {
    console.log('ici');
    this.router.navigate(['dashboard/services/add-services']);
  }
  editPrestation(id: string): void {
    this.router.navigate([`dashboard/services/edit/${id}`]);
  }

  detailPrestation(id: string): void {
    this.router.navigate([`dashboard/services/details-services/${id}`]);
  }

  deletePrestation(id: string): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Vous voulez vraiment supprimer ce Prestation !',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      cancelButtonText: 'Annuler',
      confirmButtonText: 'Oui, le supprimer !',
    }).then((result) => {
      if (result.isConfirmed) {
        this.PrestationsService.deletePrestation(id).subscribe({
          next: (response) => {
            if (response.message === 'Prestation deleted successfully') {
              this.messageService.add({
                severity: 'success',
                summary: 'Succès',
                detail: 'Prestation supprimé avec succès',
              });
              this.PrestationsResponse = this.PrestationsResponse.filter(
                (Prestation) => Prestation.trackingId !== id
              );
              Swal.fire({
                title: 'Supprimé !',
                text: 'Le Prestation a bien été supprimé.',
                icon: 'success',
              });
            } else {
              Swal.fire({
                title: 'Erreur',
                text: "Impossible de supprimer le Prestation pour l'instant, veuillez réessayer plus tard.",
                icon: 'error',
              });
            }
          },
          error: (err) => {
            console.error('Erreur lors de la suppression du Prestation :', err);
            Swal.fire({
              title: 'Erreur',
              text: 'Une erreur est survenue lors de la suppression du Prestation.',
              icon: 'error',
            });
          },
        });
      }
    });
  }
}

