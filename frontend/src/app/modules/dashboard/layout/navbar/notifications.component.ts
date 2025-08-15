import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Notification } from '../../../../models/dashboard-data';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <!-- État de chargement -->
    <div *ngIf="isLoading" class="text-center p-3">
      <div class="spinner-border spinner-border-sm text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
      <p class="mt-2 mb-0 text-muted small">Chargement des notifications...</p>
    </div>

    <!-- Liste des notifications dynamiques -->
    <ul class="list-group list-group-flush" *ngIf="!isLoading && notifications.length > 0">
      <li class="list-group-item" *ngFor="let notification of notifications">
        <div class="d-flex">
          <div class="flex-shrink-0">
            <div class="avtar avtar-s" [ngClass]="{
              'bg-light-success': notification.type === 'success',
              'bg-light-warning': notification.type === 'warning',
              'bg-light-danger': notification.type === 'error',
              'bg-light-info': notification.type === 'info'
            }">
              <i class="ph-duotone" [ngClass]="{
                'ph-check-circle': notification.type === 'success',
                'ph-warning': notification.type === 'warning',
                'ph-x-circle': notification.type === 'error',
                'ph-info': notification.type === 'info'
              }"></i>
            </div>
          </div>
          <div class="flex-grow-1 ms-3">
            <div class="d-flex">
              <div class="flex-grow-1 me-3 position-relative">
                <h6 class="mb-0 text-truncate">{{ notification.title }}</h6>
              </div>
              <div class="flex-shrink-0">
                <span class="text-sm">{{ getTimeAgo(notification.timestamp) }}</span>
              </div>
            </div>
            <p class="position-relative mt-1 mb-2">
              <span class="text-truncate">{{ notification.message }}</span>
            </p>
            <div *ngIf="notification.actionUrl" class="mt-2">
              <a [routerLink]="notification.actionUrl" 
                 class="btn btn-sm rounded-pill btn-primary">
                Voir détails
              </a>
            </div>
          </div>
        </div>
      </li>
    </ul>

    <!-- Message si aucune notification -->
    <div *ngIf="!isLoading && notifications.length === 0" class="text-center p-4">
      <div class="avtar avtar-l bg-light-secondary mx-auto mb-3">
        <i class="ph-duotone ph-bell-off f-30 text-secondary"></i>
      </div>
      <h6 class="mb-2">Aucune notification</h6>
      <p class="text-muted mb-0">Vous n'avez pas de nouvelles notifications.</p>
    </div>
  `,
  styles: []
})
export class NotificationsComponent {
  @Input() notifications: Notification[] = [];
  @Input() isLoading: boolean = false;
  @Output() markAsRead = new EventEmitter<string>();

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

  onMarkAsRead(notificationId: string): void {
    this.markAsRead.emit(notificationId);
  }
}