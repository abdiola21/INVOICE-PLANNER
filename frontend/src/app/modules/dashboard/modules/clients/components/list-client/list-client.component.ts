import { Component } from '@angular/core';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-client',
  standalone: false,
  templateUrl: './list-client.component.html',
  styleUrl: './list-client.component.css',
})
export class ListClientComponent {
  clientsResponse: ClientResponse[] = [];

  constructor(
    private clientsService: ClientsService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.getClients();
  }

  getClients(): void {
    this.clientsService.getClients().subscribe((response) => {
      if (response) {
        this.clientsResponse = response;
      }
    });
  }

  addClient(): void {
    console.log('ici');
    this.router.navigate(['dashboard/clients/add-client']);
  }

  deleteClient(id: string): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Vous voulez vraiment supprimer ce client !',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      cancelButtonText: 'Annuler',
      confirmButtonText: 'Oui, le supprimer !',
    }).then((result) => {
      if (result.isConfirmed) {
        this.clientsService.deleteClient(id).subscribe({
          next: (response) => {
            if (response.message === 'Client deleted successfully') {
              this.messageService.add({
                severity: 'success',
                summary: 'Succès',
                detail: 'Client supprimé avec succès',
              });
              this.clientsResponse = this.clientsResponse.filter(
                (client) => client.trackingId !== id
              );
              Swal.fire({
                title: 'Supprimé !',
                text: 'Le client a bien été supprimé.',
                icon: 'success',
              });
            } else {
              Swal.fire({
                title: 'Erreur',
                text: "Impossible de supprimer le client pour l'instant, veuillez réessayer plus tard.",
                icon: 'error',
              });
            }
          },
          error: (err) => {
            console.error('Erreur lors de la suppression du client :', err);
            Swal.fire({
              title: 'Erreur',
              text: 'Une erreur est survenue lors de la suppression du client.',
              icon: 'error',
            });
          },
        });
      }
    });
  }
}

