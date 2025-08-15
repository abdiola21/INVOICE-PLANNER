import { Component, OnInit, AfterViewInit, Output, EventEmitter, Renderer2, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AuthService } from '../../../../services/Auth/auth.service';
import { DashboardDataService } from '../../../../services/dashboard/dashboard-data.service';
import { Notification } from '../../../../models/dashboard-data';
import { NotificationsComponent } from './notifications.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, NotificationsComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, AfterViewInit {
  @Output() sidebarToggle = new EventEmitter<void>();

  userName: string = '';
  userEmail: string = '';
  userRole: string = '';
  userAvatarUrl: string = '';
  notificationCount: number = 0;
  menuItems: MenuItem[] = [];
  notifications: Notification[] = [];
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private dashboardDataService: DashboardDataService,
    private renderer: Renderer2,
    private host: ElementRef
  ) {}

  ngOnInit(): void {
    this.initializeUserInfo();
    this.loadNotifications();
  }

  initializeUserInfo(): void {
    const currentUser = this.authService.currentUserValue;
    if (currentUser && currentUser.user) {
      this.userName = currentUser.user.email.split('@')[0];
      this.userEmail = currentUser.user.email;
      this.userRole = currentUser.user.role;
      // this.userAvatarUrl = currentUser.user.avatar;
    }

    // Simuler des notifications pour la démo
    this.notificationCount = 3;
  }

  ngAfterViewInit(): void {
    // Ensure template is rendered, then remove quick-apps grid icon if present
    setTimeout(() => this.removeQuickAppsIcon(), 0);

    // In case the theme scripts re-inject it, observe and remove again
    const header = document.querySelector('.pc-header');
    if (header) {
      const observer = new MutationObserver(() => this.removeQuickAppsIcon());
      observer.observe(header, { childList: true, subtree: true });
    }
  }

  private removeQuickAppsIcon(): void {
    const icon = document.querySelector('.pc-header i.ph-duotone.ph-circles-four');
    if (icon) {
      const li = icon.closest('li');
      if (li && li.parentNode) {
        this.renderer.setStyle(li as Element, 'display', 'none');
      }
    }
    const dropdown = document.querySelector('.pc-header .dropdown-qta');
    if (dropdown) {
      this.renderer.setStyle(dropdown as Element, 'display', 'none');
    }
  }

  loadNotifications(): void {
    this.isLoading = true;
    this.dashboardDataService.getNotifications().subscribe({
      next: (notifications) => {
        this.notifications = notifications;
        this.notificationCount = notifications.filter(n => !n.isRead).length;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des notifications:', error);
        this.isLoading = false;
      }
    });
  }

  markNotificationAsRead(notificationId: string): void {
    this.dashboardDataService.markNotificationAsRead(notificationId);
    this.loadNotifications(); // Recharger pour mettre à jour le compteur
  }

  onNotificationAction(event: {action: any, notification: any}): void {
    console.log('Action de notification:', event.action.action, 'pour la notification:', event.notification.id);
    
    // Gérer les différentes actions
    switch (event.action.action) {
      case 'view':
        this.router.navigate([event.notification.actionUrl]);
        break;
      case 'send':
        console.log('Envoi de la notification:', event.notification.id);
        break;
      case 'mark_paid':
        console.log('Marquer comme payé:', event.notification.id);
        break;
      case 'view_profile':
        this.router.navigate(['/dashboard/clients']);
        break;
      case 'approve':
        console.log('Approuver le devis:', event.notification.id);
        break;
      case 'reject':
        console.log('Refuser le devis:', event.notification.id);
        break;
      case 'remind':
        console.log('Relancer le client:', event.notification.id);
        break;
      case 'collection':
        console.log('Mettre en recouvrement:', event.notification.id);
        break;
      case 'update':
        console.log('Mettre à jour le système');
        break;
      case 'view_service':
        this.router.navigate(['/dashboard/services']);
        break;
      default:
        console.log('Action non reconnue:', event.action.action);
    }
    
    // Marquer comme lue après action
    this.markNotificationAsRead(event.notification.id);
  }

  archiveAllNotifications(): void {
    // Implémenter l'archivage de toutes les notifications
    console.log('Archivage de toutes les notifications');
  }

  markAllAsRead(): void {
    // Marquer toutes les notifications comme lues
    this.notifications.forEach(notification => {
      if (!notification.isRead) {
        this.dashboardDataService.markNotificationAsRead(notification.id);
      }
    });
    this.loadNotifications(); // Recharger pour mettre à jour le compteur
  }

  getTimeAgo(timestamp: Date): string {
    const now = new Date();
    const diffInMs = now.getTime() - timestamp.getTime();
    const diffInMinutes = Math.floor(diffInMs / (1000 * 60));
    const diffInHours = Math.floor(diffInMs / (1000 * 60 * 60));
    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24));

    if (diffInMinutes < 1) {
      return 'À l\'instant';
    } else if (diffInMinutes < 60) {
      return `Il y a ${diffInMinutes} min`;
    } else if (diffInHours < 24) {
      return `Il y a ${diffInHours}h`;
    } else {
      return `Il y a ${diffInDays}j`;
    }
  }



  logout(): void {
    this.authService.logout();
  }
}

