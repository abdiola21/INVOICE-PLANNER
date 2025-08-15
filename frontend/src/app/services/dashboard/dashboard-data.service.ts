import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { DashboardData, SidebarData, NavbarData, MenuItem, UserCardData, QuickAction, Notification, StatItem } from '../../models/dashboard-data';

@Injectable({
  providedIn: 'root'
})
export class DashboardDataService {
  private dashboardData: DashboardData = {
    sidebar: {
      logo: {
        imageUrl: 'assets/images/logo/invoiceslogan.png',
        imageAlt: 'InvoicePlanner Logo',
        title: 'Invoice Planner',
        subtitle: 'Gestion de factures'
      },
      menuItems: [
        {
          id: '1',
          text: 'Tableau de bord',
          icon: 'bx bxs-dashboard',
          route: '/dashboard',
          order: 1,
          isActive: true
        },
        {
          id: '2',
          text: 'Clients',
          icon: 'bx bx-user',
          route: '/dashboard/clients',
          order: 2,
          isActive: false
        },
        {
          id: '3',
          text: 'Devis',
          icon: 'ph-duotone ph-file-text',
          route: '/dashboard/devis',
          order: 3,
          isActive: false
        },
        {
          id: '4',
          text: 'Factures',
          icon: 'ph-duotone ph-receipt',
          route: '/dashboard/factures',
          order: 4,
          isActive: false
        },
        {
          id: '5',
          text: 'Services',
          icon: 'bx bx-shopping-bag',
          route: '/dashboard/services',
          order: 5,
          isActive: false
        }
      ],
      userCard: {
        avatarUrl: 'assets/images/user/avatar-1.jpg',
        userName: 'Utilisateur',
        userEmail: 'user@example.com',
        userRole: 'User',
        profileLink: '/profile',
        settingsLink: '/dashboard/profile'
      }
    },
    navbar: {
      searchPlaceholder: 'Search...',
      searchShortcut: 'ctrl+k',
      quickActions: [
        {
          id: '1',
          text: 'E-commerce',
          icon: 'ph-duotone ph-shopping-cart',
          url: '#!',
          order: 1,
          category: 'business'
        },
        {
          id: '2',
          text: 'Helpdesk',
          icon: 'ph-duotone ph-lifebuoy',
          url: '#!',
          order: 2,
          category: 'support'
        },
        {
          id: '3',
          text: 'Invoice',
          icon: 'ph-duotone ph-scroll',
          url: '#!',
          order: 3,
          category: 'business'
        },
        {
          id: '4',
          text: 'Online Courses',
          icon: 'ph-duotone ph-books',
          url: '#!',
          order: 4,
          category: 'education'
        },
        {
          id: '5',
          text: 'Mail',
          icon: 'ph-duotone ph-envelope-open',
          url: '#!',
          order: 5,
          category: 'communication'
        },
        {
          id: '6',
          text: 'Membership',
          icon: 'ph-duotone ph-identification-badge',
          url: '#!',
          order: 6,
          category: 'business'
        },
        {
          id: '7',
          text: 'Chat',
          icon: 'ph-duotone ph-chats-circle',
          url: '#!',
          order: 7,
          category: 'communication'
        },
        {
          id: '8',
          text: 'Plans',
          icon: 'ph-duotone ph-currency-circle-dollar',
          url: '#!',
          order: 8,
          category: 'business'
        },
        {
          id: '9',
          text: 'Users',
          icon: 'ph-duotone ph-user-circle',
          url: '#!',
          order: 9,
          category: 'management'
        }
      ],
      themeToggle: {
        lightIcon: 'ph-duotone ph-sun-dim',
        darkIcon: 'ph-duotone ph-moon',
        lightText: 'Light',
        darkText: 'Dark'
      },
      notifications: [
        {
          id: '1',
          title: 'Nouvelle facture',
          message: 'La facture #F001 a été créée avec succès',
          type: 'success',
          timestamp: new Date(),
          isRead: false,
          actionUrl: '/dashboard/factures',
          order: 1
        },
        {
          id: '2',
          title: 'Paiement reçu',
          message: 'Le paiement pour la facture #F001 a été reçu',
          type: 'success',
          timestamp: new Date(Date.now() - 3600000),
          isRead: false,
          actionUrl: '/dashboard/factures',
          order: 2
        },
        {
          id: '3',
          title: 'Client ajouté',
          message: 'Le client "Entreprise ABC" a été ajouté',
          type: 'info',
          timestamp: new Date(Date.now() - 7200000),
          isRead: true,
          actionUrl: '/dashboard/clients',
          order: 3
        }
      ],
      userMenu: {
        profileLink: '/profile',
        settingsLink: '/dashboard/profile',
        logoutText: 'Déconnexion'
      }
    },
    userProfile: {
      id: '1',
      email: 'user@example.com',
      firstName: 'Utilisateur',
      lastName: 'Test',
      role: 'USER',
      avatarUrl: 'assets/images/user/avatar-1.jpg',
      company: 'Ma Société',
      phone: '+225 0123456789',
      address: 'Abidjan, Côte d\'Ivoire',
      preferences: {
        theme: 'light',
        language: 'fr',
        timezone: 'Africa/Abidjan',
        notifications: {
          email: true,
          push: true,
          sms: false,
          frequency: 'immediate'
        }
      }
    },
    quickActions: [
      {
        id: '1',
        text: 'Nouveau devis',
        icon: 'ph-duotone ph-file-text',
        url: '/dashboard/devis/new',
        order: 1,
        category: 'devis'
      },
      {
        id: '2',
        text: 'Nouvelle facture',
        icon: 'ph-duotone ph-receipt',
        url: '/dashboard/factures/new',
        order: 2,
        category: 'facture'
      },
      {
        id: '3',
        text: 'Nouveau client',
        icon: 'ph-duotone ph-user-plus',
        url: '/dashboard/clients/new',
        order: 3,
        category: 'client'
      },
      {
        id: '4',
        text: 'Nouveau service',
        icon: 'ph-duotone ph-plus-circle',
        url: '/dashboard/services/new',
        order: 4,
        category: 'service'
      }
    ],
    notifications: [
      {
        id: '1',
        title: 'Nouvelle facture',
        message: 'La facture #F001 a été créée avec succès',
        type: 'success',
        timestamp: new Date(),
        isRead: false,
        actionUrl: '/dashboard/factures',
        order: 1
      },
      {
        id: '2',
        title: 'Paiement reçu',
        message: 'Le paiement pour la facture #F001 a été reçu',
        type: 'success',
        timestamp: new Date(Date.now() - 3600000),
        isRead: false,
        actionUrl: '/dashboard/factures',
        order: 2
      },
      {
        id: '3',
        title: 'Client ajouté',
        message: 'Le client "Entreprise ABC" a été ajouté',
        type: 'info',
        timestamp: new Date(Date.now() - 7200000),
        isRead: true,
        actionUrl: '/dashboard/clients',
        order: 3
      }
    ],
    stats: [
      {
        id: '1',
        title: 'Chiffre d\'affaires',
        value: '2,450,000 Fcfa',
        change: 12.5,
        changeType: 'increase',
        icon: 'ph-duotone ph-trend-up',
        color: 'success',
        order: 1
      },
      {
        id: '2',
        title: 'Factures payées',
        value: '45',
        change: 8.2,
        changeType: 'increase',
        icon: 'ph-duotone ph-check-circle',
        color: 'success',
        order: 2
      },
      {
        id: '3',
        title: 'Clients actifs',
        value: '28',
        change: -2.1,
        changeType: 'decrease',
        icon: 'ph-duotone ph-users',
        color: 'warning',
        order: 3
      },
      {
        id: '4',
        title: 'Devis en attente',
        value: '12',
        change: 15.3,
        changeType: 'increase',
        icon: 'ph-duotone ph-clock',
        color: 'info',
        order: 4
      }
    ]
  };

  constructor() { }

  getDashboardData(): Observable<DashboardData> {
    return of(this.dashboardData);
  }

  getSidebarData(): Observable<SidebarData> {
    return of(this.dashboardData.sidebar);
  }

  getNavbarData(): Observable<NavbarData> {
    return of(this.dashboardData.navbar);
  }

  getMenuItems(): Observable<MenuItem[]> {
    return of(this.dashboardData.sidebar.menuItems.sort((a, b) => a.order - b.order));
  }

  getUserCardData(): Observable<UserCardData> {
    return of(this.dashboardData.sidebar.userCard);
  }

  getQuickActions(): Observable<QuickAction[]> {
    return of(this.dashboardData.quickActions.sort((a, b) => a.order - b.order));
  }

  getNotifications(): Observable<Notification[]> {
    // Générer des notifications dynamiques et réalistes
    const dynamicNotifications: Notification[] = [
      {
        id: '1',
        title: 'Nouvelle facture créée',
        message: 'La facture <strong>#F001</strong> pour <strong>Entreprise ABC</strong> a été créée avec succès.',
        type: 'success',
        timestamp: new Date(Date.now() - 300000), // 5 minutes ago
        isRead: false,
        actionUrl: '/dashboard/factures',
        order: 1,
        tags: ['facture', 'création'],
        actions: [
          { type: 'primary', label: 'Voir facture', action: 'view' },
          { type: 'secondary', label: 'Envoyer', action: 'send' }
        ]
      },
      {
        id: '2',
        title: 'Paiement reçu',
        message: 'Le paiement de <strong>150,000 Fcfa</strong> pour la facture <strong>#F001</strong> a été reçu.',
        type: 'success',
        timestamp: new Date(Date.now() - 1800000), // 30 minutes ago
        isRead: false,
        actionUrl: '/dashboard/factures',
        order: 2,
        tags: ['paiement', 'reçu'],
        actions: [
          { type: 'success', label: 'Marquer comme payé', action: 'mark_paid' }
        ]
      },
      {
        id: '3',
        title: 'Nouveau client ajouté',
        message: 'Le client <strong>Tech Solutions SARL</strong> a été ajouté à votre base de données.',
        type: 'info',
        timestamp: new Date(Date.now() - 3600000), // 1 hour ago
        isRead: true,
        actionUrl: '/dashboard/clients',
        order: 3,
        tags: ['client', 'nouveau'],
        actions: [
          { type: 'primary', label: 'Voir profil', action: 'view_profile' }
        ]
      },
      {
        id: '4',
        title: 'Devis en attente',
        message: 'Le devis <strong>#D005</strong> pour <strong>Digital Agency</strong> est en attente de validation.',
        type: 'warning',
        timestamp: new Date(Date.now() - 7200000), // 2 hours ago
        isRead: false,
        actionUrl: '/dashboard/devis',
        order: 4,
        tags: ['devis', 'en attente'],
        actions: [
          { type: 'primary', label: 'Valider', action: 'approve' },
          { type: 'danger', label: 'Refuser', action: 'reject' }
        ]
      },
      {
        id: '5',
        title: 'Facture en retard',
        message: 'La facture <strong>#F003</strong> pour <strong>Startup XYZ</strong> est en retard de paiement.',
        type: 'error',
        timestamp: new Date(Date.now() - 86400000), // 1 day ago
        isRead: false,
        actionUrl: '/dashboard/factures',
        order: 5,
        tags: ['retard', 'urgent'],
        actions: [
          { type: 'warning', label: 'Relancer', action: 'remind' },
          { type: 'danger', label: 'Mettre en recouvrement', action: 'collection' }
        ]
      },
      {
        id: '6',
        title: 'Mise à jour système',
        message: 'Une nouvelle version de l\'application est disponible avec des améliorations de sécurité.',
        type: 'info',
        timestamp: new Date(Date.now() - 172800000), // 2 days ago
        isRead: true,
        actionUrl: '/dashboard/settings',
        order: 6,
        tags: ['système', 'mise à jour'],
        actions: [
          { type: 'primary', label: 'Mettre à jour', action: 'update' }
        ]
      },
      {
        id: '7',
        title: 'Nouveau service créé',
        message: 'Le service <strong>Consultation Web</strong> a été ajouté à votre catalogue.',
        type: 'success',
        timestamp: new Date(Date.now() - 259200000), // 3 days ago
        isRead: true,
        actionUrl: '/dashboard/services',
        order: 7,
        tags: ['service', 'nouveau'],
        actions: [
          { type: 'primary', label: 'Voir service', action: 'view_service' }
        ]
      }
    ];

    return of(dynamicNotifications.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()));
  }

  getStats(): Observable<StatItem[]> {
    return of(this.dashboardData.stats.sort((a, b) => a.order - b.order));
  }

  getUserProfile(): Observable<any> {
    return of(this.dashboardData.userProfile);
  }

  // Méthodes pour mettre à jour dynamiquement les données
  updateMenuItems(menuItems: MenuItem[]): void {
    this.dashboardData.sidebar.menuItems = menuItems;
  }

  updateUserCardData(userCard: Partial<UserCardData>): void {
    this.dashboardData.sidebar.userCard = { ...this.dashboardData.sidebar.userCard, ...userCard };
  }

  addQuickAction(action: QuickAction): void {
    this.dashboardData.quickActions.push(action);
    this.dashboardData.quickActions.sort((a, b) => a.order - b.order);
  }

  updateQuickAction(id: string, updates: Partial<QuickAction>): void {
    const index = this.dashboardData.quickActions.findIndex(a => a.id === id);
    if (index !== -1) {
      this.dashboardData.quickActions[index] = { ...this.dashboardData.quickActions[index], ...updates };
    }
  }

  removeQuickAction(id: string): void {
    this.dashboardData.quickActions = this.dashboardData.quickActions.filter(a => a.id !== id);
  }

  addNotification(notification: Notification): void {
    this.dashboardData.navbar.notifications.push(notification);
    this.dashboardData.navbar.notifications.sort((a, b) => a.order - b.order);
  }

  updateNotification(id: string, updates: Partial<Notification>): void {
    const index = this.dashboardData.navbar.notifications.findIndex(n => n.id === id);
    if (index !== -1) {
      this.dashboardData.navbar.notifications[index] = { ...this.dashboardData.navbar.notifications[index], ...updates };
    }
  }

  removeNotification(id: string): void {
    this.dashboardData.navbar.notifications = this.dashboardData.navbar.notifications.filter(n => n.id !== id);
  }

  markNotificationAsRead(id: string): void {
    const notification = this.dashboardData.navbar.notifications.find(n => n.id === id);
    if (notification) {
      notification.isRead = true;
    }
  }

  addStat(stat: StatItem): void {
    this.dashboardData.stats.push(stat);
    this.dashboardData.stats.sort((a, b) => a.order - b.order);
  }

  updateStat(id: string, updates: Partial<StatItem>): void {
    const index = this.dashboardData.stats.findIndex(s => s.id === id);
    if (index !== -1) {
      this.dashboardData.stats[index] = { ...this.dashboardData.stats[index], ...updates };
    }
  }

  removeStat(id: string): void {
    this.dashboardData.stats = this.dashboardData.stats.filter(s => s.id !== id);
  }

  updateUserProfile(profile: Partial<any>): void {
    this.dashboardData.userProfile = { ...this.dashboardData.userProfile, ...profile };
  }
}
