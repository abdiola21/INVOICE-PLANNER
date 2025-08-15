import { Component, OnInit, Input, AfterViewInit, Renderer2, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../services/Auth/auth.service';
import { DashboardDataService } from '../../../../services/dashboard/dashboard-data.service';
import { AppConfigService } from '../../../../services/config/app-config.service';
import { MenuItem, UserCardData } from '../../../../models/dashboard-data';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit, AfterViewInit {
  @Input() isCollapsed: boolean = false;

  // Données dynamiques
  menuItems: MenuItem[] = [];
  userCard: UserCardData | null = null;
  logo: any = null;
  
  // États de chargement
  isLoading = true;
  error: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private dashboardDataService: DashboardDataService,
    private appConfigService: AppConfigService,
    private renderer: Renderer2,
    private host: ElementRef
  ) {}

  ngOnInit(): void {
    this.loadSidebarData();
    this.initializeUserInfo();
  }

  private loadSidebarData(): void {
    this.isLoading = true;
    this.error = null;

    // Charger la configuration de l'application
    this.appConfigService.config$.subscribe(config => {
      this.logo = config.branding.logo;
    });

    // Charger les données de la sidebar
    Promise.all([
      this.dashboardDataService.getMenuItems().toPromise(),
      this.dashboardDataService.getUserCardData().toPromise()
    ]).then(([menuItems, userCard]) => {
      this.menuItems = menuItems || [];
      this.userCard = userCard || null;
      this.isLoading = false;
    }).catch(error => {
      console.error('Erreur lors du chargement des données de la sidebar:', error);
      this.error = 'Erreur lors du chargement des données';
      this.isLoading = false;
    });
  }

  private initializeUserInfo(): void {
    const currentUser = this.authService.currentUserValue;
    if (currentUser && currentUser.user) {
      // Utiliser le nom de l'email comme fallback, mais essayer de récupérer le vrai nom
      const userName = currentUser.user.email.split('@')[0];
      
      // Mettre à jour les informations utilisateur dans le service
      this.dashboardDataService.updateUserCardData({
        userName: userName,
        userEmail: currentUser.user.email,
        userRole: currentUser.user.role === 'ADMIN' ? 'Admin' : 'User'
      });
    }
  }

  ngAfterViewInit(): void {
    // Force the brand logo from config (avoid theme.js overriding with template logo)
    const applyLogo = () => {
      const branding = this.appConfigService.getConfigValue('branding');
      const theme = document.body.getAttribute('data-pc-theme') as 'light' | 'dark' | 'auto' | null;
      const targetSrc = theme === 'dark' && branding.logo.secondary ? branding.logo.secondary : branding.logo.primary;
      const img: HTMLImageElement | null = document.querySelector('.pc-sidebar .m-header .logo-lg');
      if (img && img.src.indexOf(targetSrc) === -1) {
        this.renderer.setAttribute(img, 'src', targetSrc);
        this.renderer.setAttribute(img, 'alt', branding.logo.alt || 'InvoicePlanner Logo');
      }
    };

    // Initial apply after view init
    setTimeout(applyLogo, 0);

    // Observe theme switches and sidebar header mutations
    const bodyObserver = new MutationObserver(applyLogo);
    bodyObserver.observe(document.body, { attributes: true, attributeFilter: ['data-pc-theme'] });

    const header: Element | null = document.querySelector('.pc-sidebar .m-header');
    if (header) {
      const headerObserver = new MutationObserver(applyLogo);
      headerObserver.observe(header, { childList: true, subtree: true, attributes: true });
    }

    // Update on config changes as well
    this.appConfigService.config$.subscribe(() => applyLogo());
  }

  // Méthodes de navigation
  onMenuItemClick(menuItem: MenuItem): void {
    // Marquer l'élément comme actif
    this.menuItems.forEach(item => item.isActive = false);
    menuItem.isActive = true;
    
    // Naviguer vers la route
    this.router.navigate([menuItem.route]);
  }

  // Méthodes pour la gestion dynamique
  refreshSidebarData(): void {
    this.loadSidebarData();
  }

  // Méthodes pour la personnalisation dynamique
  updateMenuItems(newMenuItems: MenuItem[]): void {
    this.dashboardDataService.updateMenuItems(newMenuItems);
    this.menuItems = newMenuItems;
  }

  addMenuItem(menuItem: MenuItem): void {
    this.dashboardDataService.updateMenuItems([...this.menuItems, menuItem]);
    this.menuItems.push(menuItem);
    this.menuItems.sort((a, b) => a.order - b.order);
  }

  removeMenuItem(menuItemId: string): void {
    const filteredItems = this.menuItems.filter(item => item.id !== menuItemId);
    this.dashboardDataService.updateMenuItems(filteredItems);
    this.menuItems = filteredItems;
  }

  updateUserCard(updates: Partial<UserCardData>): void {
    if (this.userCard) {
      this.dashboardDataService.updateUserCardData(updates);
      this.userCard = { ...this.userCard, ...updates };
    }
  }

  logout(): void {
    this.authService.logout();
  }

  // Méthodes utilitaires
  hasPermission(permission: string): boolean {
    return this.appConfigService.hasPermission(permission);
  }

  isFeatureEnabled(featureId: string): boolean {
    return this.appConfigService.isFeatureEnabled(featureId);
  }

  // Méthodes pour la gestion des thèmes
  toggleTheme(): void {
    const currentTheme = this.appConfigService.getConfigValue('theme').defaultTheme;
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    this.appConfigService.updateTheme({ defaultTheme: newTheme });
  }
}

