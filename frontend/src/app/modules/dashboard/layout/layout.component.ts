import { Component, OnInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule, NgClass } from '@angular/common';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
  standalone: true,
  imports: [RouterModule, CommonModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LayoutComponent implements OnInit {
  isSidebarCollapsed: boolean = false;

  constructor() { }

  ngOnInit(): void {
    // Vérifier si l'état de la barre latérale est enregistré dans le localStorage
    const savedState = localStorage.getItem('sidebarCollapsed');
    if (savedState) {
      this.isSidebarCollapsed = JSON.parse(savedState);
    }

    // Ajuster la mise en page en fonction de la taille de l'écran
    this.adjustLayoutForScreenSize();

    // Écouter les changements de taille d'écran
    window.addEventListener('resize', this.adjustLayoutForScreenSize.bind(this));
  }

  toggleSidebar(): void {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
    // Sauvegarder l'état dans le localStorage
    localStorage.setItem('sidebarCollapsed', JSON.stringify(this.isSidebarCollapsed));
  }

  private adjustLayoutForScreenSize(): void {
    // Réduire automatiquement la barre latérale sur les petits écrans
    if (window.innerWidth < 992) {
      this.isSidebarCollapsed = true;
    }
  }
}

