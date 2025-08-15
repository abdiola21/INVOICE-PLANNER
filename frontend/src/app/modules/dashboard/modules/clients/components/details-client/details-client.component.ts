import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientsService } from '../../../../../../services/Clients/clients.service';
import { ClientResponse } from '../../../../../../models/responses/Clients/client-response';
import { forkJoin, catchError, of } from 'rxjs';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-details-client',
  templateUrl: './details-client.component.html',
  styleUrls: ['./details-client.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, NgIf, NgFor]
})
export class DetailsClientComponent implements OnInit {
  client!: ClientResponse;
  otherClients: ClientResponse[] = [];
  loading = false;
  error: string | null = null;
  maxOtherClients = 5; // Nombre maximum d'autres clients à afficher

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clientService: ClientsService
  ) {}

  ngOnInit(): void {
    this.getClientDetails();
  }

  private getClientDetails() {
    this.loading = true;
    this.error = null;

    this.route.paramMap.subscribe(params => {
      const clientId = params.get('id');
      if (clientId) {
        console.log('Récupération des détails pour le client ID:', clientId);
        
        forkJoin({
          currentClient: this.clientService.getClientById(clientId).pipe(
            catchError(err => {
              console.error('Erreur lors de la récupération du client actuel:', err);
              return of(null);
            })
          ),
          allClients: this.clientService.getClients().pipe(
            catchError(err => {
              console.error('Erreur lors de la récupération de tous les clients:', err);
              return of([]);
            })
          )
        }).subscribe(
          (result) => {
            console.log('Données client reçues:', result.currentClient);
            console.log('Liste des clients reçue:', result.allClients);
            
            if (result.currentClient) {
              this.client = result.currentClient;
              this.otherClients = result.allClients
                .filter((c: ClientResponse) => c.trackingId !== clientId)
                .slice(0, this.maxOtherClients);
            } else {
              this.error = 'Client non trouvé';
            }
            this.loading = false;
          },
          (error) => {
            this.error = 'Erreur lors de la récupération des données';
            console.error('Erreur lors de la récupération des données client:', error);
            this.loading = false;
          }
        );
      } else {
        this.error = 'ID client non spécifié';
        this.loading = false;
      }
    });
  }

  navigateToAddClient() {
    this.router.navigate(['/dashboard/clients/add-client']);
  }

  // nombre de factures associées au client
  // getFactureCount(): number {
  //   return this.client.factures.length;
  // }

  // // nombre de devis associées au client
  // getDevisCount(): number {
  //   return this.client.devis.length;
  // }
}

