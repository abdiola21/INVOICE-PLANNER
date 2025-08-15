import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor, NgClass, DatePipe, SlicePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FactureService } from '../../../../services/facture/facture.service';
import { DevisService } from '../../../../services/devis/devis.service';
import { ClientsService } from '../../../../services/Clients/clients.service';
import { PrestationsService } from '../../../../services/Prestations/prestations.service';
import { forkJoin } from 'rxjs';

// Définition des états possibles d'une facture (doit correspondre à l'enum côté backend)
export enum EtatFacture {
  BROUILLON = 'BROUILLON',
  ENVOYEE = 'ENVOYEE',
  PAYEE = 'PAYEE',
  RETARD = 'RETARD',
  ANNULEE = 'ANNULEE'
}

@Component({
  selector: 'app-body',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule,
    NgIf,
    NgFor,
    // NgClass,
    DatePipe,
    SlicePipe
  ],
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.scss']
})
export class BodyComponent implements OnInit {

  // Données récupérées
  factures: any[] = [];
  devis: any[] = [];
  clients: any[] = [];
  prestations: any[] = [];

  // Données filtrées
  facturesRecentes: any[] = [];
  clientsRecents: any[] = [];
  facturesMois: any[] = [];

  // Statistiques calculées
  totalFactures: number = 0;
  totalDevis: number = 0;
  totalClients: number = 0;
  totalPrestations: number = 0;

  montantFacturesMois: number = 0;
  montantFacturesAnnee: number = 0;

  facturesPayees: number = 0;
  facturesEnAttente: number = 0;
  facturesRetard: number = 0;
  facturesBrouillon: number = 0;
  facturesAnnulees: number = 0;
  facturesEnvoyees: number = 0;

  devisAcceptes: number = 0;
  devisEnAttente: number = 0;
  devisRefuses: number = 0;

  // États de l'interface
  isLoading: boolean = true;
  hasError: boolean = false;

  constructor(
    private factureService: FactureService,
    private devisService: DevisService,
    private clientsService: ClientsService,
    private prestationsService: PrestationsService
  ) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.hasError = false;

    // Charger toutes les données en parallèle
    forkJoin({
      factures: this.factureService.findAll(),
      devis: this.devisService.findAll(),
      clients: this.clientsService.getClients(),
      prestations: this.prestationsService.getPrestations()
    }).subscribe({
      next: (data) => {
        // Stocker les données (extraire les données des réponses)
        this.factures = data.factures.data || [];
        this.devis = data.devis.data || [];
        this.clients = data.clients || [];
        this.prestations = data.prestations || [];

        // Calculer les statistiques
        this.calculateStatistics();
        
        // Préparer les données pour l'affichage
        this.prepareDisplayData();
        
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des données du dashboard', err);
        this.isLoading = false;
        this.hasError = true;
      }
    });
  }

  private calculateStatistics(): void {
    // Totaux
    this.totalFactures = this.factures.length;
    this.totalDevis = this.devis.length;
    this.totalClients = this.clients.length;
    this.totalPrestations = this.prestations.length;

    // Statistiques des factures
    const now = new Date();
    const currentMonth = now.getMonth();
    const currentYear = now.getFullYear();

    // Factures du mois et de l'année en cours
    this.facturesMois = this.factures.filter(f => {
      const dateFacture = new Date(f.dateEmission || f.dateFacture);
      return dateFacture.getMonth() === currentMonth && dateFacture.getFullYear() === currentYear;
    });

    const facturesAnnee = this.factures.filter(f => {
      const dateFacture = new Date(f.dateEmission || f.dateFacture);
      return dateFacture.getFullYear() === currentYear;
    });

    // Calculer les montants
    this.montantFacturesMois = this.facturesMois.reduce((sum, f) => sum + (f.montantTTC || f.montantTotal || 0), 0);
    this.montantFacturesAnnee = facturesAnnee.reduce((sum, f) => sum + (f.montantTTC || f.montantTotal || 0), 0);

    // Statuts des factures (en utilisant l'enum pour plus de clarté)
    this.facturesPayees = this.factures.filter(f => f.etat === EtatFacture.PAYEE || f.etat === 'PAYE').length;
    this.facturesEnAttente = this.factures.filter(f => f.etat === EtatFacture.ENVOYEE || f.etat === 'ENVOYEE').length;
    this.facturesRetard = this.factures.filter(f => {
      if (f.etat !== EtatFacture.ENVOYEE && f.etat !== EtatFacture.PAYEE) return false;
      const dateEcheance = new Date(f.dateEcheance);
      return dateEcheance < now;
    }).length;
    this.facturesBrouillon = this.factures.filter(f => f.etat === EtatFacture.BROUILLON || f.etat === 'BROUILLON').length;
    this.facturesAnnulees = this.factures.filter(f => f.etat === EtatFacture.ANNULEE || f.etat === 'ANNULEE').length;
    this.facturesEnvoyees = this.factures.filter(f => f.etat === EtatFacture.ENVOYEE || f.etat === 'ENVOYEE').length;

    // Statuts des devis
    this.devisAcceptes = this.devis.filter(d => d.statut === 'ACCEPTE' || d.etat === 'ACCEPTE').length;
    this.devisEnAttente = this.devis.filter(d => d.statut === 'ENVOYEE' || d.etat === 'ENVOYEE').length;
    this.devisRefuses = this.devis.filter(d => d.statut === 'REFUSE' || d.etat === 'REFUSE').length;
  }

  private prepareDisplayData(): void {
    // Préparer les factures récentes (triées par date décroissante)
    this.facturesRecentes = [...this.factures]
      .sort((a, b) => {
        const dateA = new Date(a.dateEmission || a.dateFacture || new Date());
        const dateB = new Date(b.dateEmission || b.dateFacture || new Date());
        return dateB.getTime() - dateA.getTime();
      })
      .slice(0, 10); // Limiter aux 10 plus récentes

    // Enrichir les factures avec les données clients
    this.facturesRecentes.forEach(facture => {
      if (facture.clientId) {
        facture.client = this.clients.find(c => c.id === facture.clientId || c.trackingId === facture.clientId);
      }
    });

    // Préparer les clients récents (triés par date de création décroissante)
    this.clientsRecents = [...this.clients]
      .sort((a, b) => {
        // Utiliser les différentes propriétés possibles pour la date
        const dateA = new Date(a.dateCreation || a.createdAt || a.created_at || new Date());
        const dateB = new Date(b.dateCreation || b.createdAt || b.created_at || new Date());
        return dateB.getTime() - dateA.getTime();
      })
      .slice(0, 10); // Limiter aux 10 plus récents
  }

  // Méthodes utilitaires
  calculateProgress(value: number, total: number): number {
    if (total === 0) return 0;
    return Math.round((value / total) * 100);
  }

  // Formater les montants avec le symbole de devise FCFA
  formatMontant(montant: number): string {
    // Affichage en FCFA (XOF) sans le code devise, mais avec "FCFA"
    const montantFormate = new Intl.NumberFormat('fr-FR', { minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(montant || 0);
    return `${montantFormate} FCFA`;
  }
}

