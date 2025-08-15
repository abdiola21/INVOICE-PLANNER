import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AppConfigService } from '../config/app-config.service';

export type ThemeType = 'light' | 'dark' | 'auto';

export interface ThemeConfig {
  id: string;
  name: string;
  description: string;
  isActive: boolean;
  isDefault: boolean;
  colors: ThemeColors;
}

export interface ThemeColors {
  primary: string;
  secondary: string;
  accent: string;
  success: string;
  warning: string;
  danger: string;
  info: string;
  light: string;
  dark: string;
  background: string;
  surface: string;
  text: string;
  textSecondary: string;
  border: string;
  shadow: string;
}

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private renderer: Renderer2;
  private currentThemeSubject = new BehaviorSubject<ThemeType>('light');
  public currentTheme$ = this.currentThemeSubject.asObservable();

  private readonly THEME_STORAGE_KEY = 'selectedTheme';
  private readonly THEME_ATTRIBUTE = 'data-theme';

  constructor(
    private rendererFactory: RendererFactory2,
    private appConfigService: AppConfigService
  ) {
    this.renderer = this.rendererFactory.createRenderer(null, null);
    this.initializeTheme();
  }

  private initializeTheme(): void {
    // Charger le thème depuis le localStorage ou utiliser le thème par défaut
    const savedTheme = localStorage.getItem(this.THEME_STORAGE_KEY);
    const defaultTheme = this.appConfigService.getConfigValue('theme').defaultTheme;
    const themeToApply = (savedTheme || defaultTheme) as ThemeType;
    
    this.applyTheme(themeToApply);
  }

  getCurrentTheme(): ThemeType {
    return this.currentThemeSubject.value;
  }

  setTheme(theme: ThemeType): void {
    this.applyTheme(theme);
    this.currentThemeSubject.next(theme);
    localStorage.setItem(this.THEME_STORAGE_KEY, theme);
    
    // Mettre à jour la configuration de l'application
    this.appConfigService.updateTheme({ defaultTheme: theme });
  }

  private applyTheme(theme: ThemeType): void {
    const root = document.documentElement;
    
    // Supprimer tous les attributs de thème existants
    this.renderer.removeAttribute(root, this.THEME_ATTRIBUTE);
    this.renderer.removeAttribute(root, 'class');
    
    // Appliquer le nouveau thème
    if (theme === 'auto') {
      this.applyAutoTheme();
    } else {
      this.renderer.setAttribute(root, this.THEME_ATTRIBUTE, theme);
      this.renderer.addClass(root, `theme-${theme}`);
      this.applyThemeColors(theme);
    }
  }

  private applyAutoTheme(): void {
    const hour = new Date().getHours();
    const isDaytime = hour >= 6 && hour < 20;
    const theme = isDaytime ? 'light' : 'dark';
    
    const root = document.documentElement;
    this.renderer.setAttribute(root, this.THEME_ATTRIBUTE, theme);
    this.renderer.addClass(root, `theme-${theme}`);
    this.applyThemeColors(theme);
    
    // Mettre à jour le thème courant
    this.currentThemeSubject.next(theme);
  }

  private applyThemeColors(theme: ThemeType): void {
    const root = document.documentElement;
    const config = this.appConfigService.getConfig();
    
    if (theme === 'light') {
      this.applyLightThemeColors(root, config);
    } else if (theme === 'dark') {
      this.applyDarkThemeColors(root, config);
    }
  }

  private applyLightThemeColors(root: HTMLElement, config: any): void {
    const colors = config.branding.colors;
    
    // Couleurs principales
    this.renderer.setStyle(root, '--primary-color', colors.primary);
    this.renderer.setStyle(root, '--secondary-color', colors.secondary);
    this.renderer.setStyle(root, '--accent-color', colors.accent);
    
    // Couleurs de statut
    this.renderer.setStyle(root, '--success-color', colors.success);
    this.renderer.setStyle(root, '--warning-color', colors.warning);
    this.renderer.setStyle(root, '--danger-color', colors.danger);
    this.renderer.setStyle(root, '--info-color', colors.info);
    
    // Couleurs de base
    this.renderer.setStyle(root, '--light-color', colors.light);
    this.renderer.setStyle(root, '--dark-color', colors.dark);
    
    // Couleurs d'interface
    this.renderer.setStyle(root, '--background-color', '#ffffff');
    this.renderer.setStyle(root, '--surface-color', '#f8f9fa');
    this.renderer.setStyle(root, '--text-color', '#212529');
    this.renderer.setStyle(root, '--text-secondary-color', '#6c757d');
    this.renderer.setStyle(root, '--border-color', '#dee2e6');
    this.renderer.setStyle(root, '--shadow-color', 'rgba(0, 0, 0, 0.1)');
  }

  private applyDarkThemeColors(root: HTMLElement, config: any): void {
    const colors = config.branding.colors;
    
    // Couleurs principales
    this.renderer.setStyle(root, '--primary-color', colors.primary);
    this.renderer.setStyle(root, '--secondary-color', colors.secondary);
    this.renderer.setStyle(root, '--accent-color', colors.accent);
    
    // Couleurs de statut
    this.renderer.setStyle(root, '--success-color', colors.success);
    this.renderer.setStyle(root, '--warning-color', colors.warning);
    this.renderer.setStyle(root, '--danger-color', colors.danger);
    this.renderer.setStyle(root, '--info-color', colors.info);
    
    // Couleurs de base
    this.renderer.setStyle(root, '--light-color', colors.light);
    this.renderer.setStyle(root, '--dark-color', colors.dark);
    
    // Couleurs d'interface (mode sombre)
    this.renderer.setStyle(root, '--background-color', '#1a1a1a');
    this.renderer.setStyle(root, '--surface-color', '#2d2d2d');
    this.renderer.setStyle(root, '--text-color', '#ffffff');
    this.renderer.setStyle(root, '--text-secondary-color', '#b0b0b0');
    this.renderer.setStyle(root, '--border-color', '#404040');
    this.renderer.setStyle(root, '--shadow-color', 'rgba(0, 0, 0, 0.3)');
  }

  // Méthodes pour la personnalisation dynamique des couleurs
  updatePrimaryColor(color: string): void {
    const root = document.documentElement;
    this.renderer.setStyle(root, '--primary-color', color);
    
    // Mettre à jour la configuration
    this.appConfigService.updateBranding({
      colors: { ...this.appConfigService.getConfig().branding.colors, primary: color }
    });
  }

  updateSecondaryColor(color: string): void {
    const root = document.documentElement;
    this.renderer.setStyle(root, '--secondary-color', color);
    
    // Mettre à jour la configuration
    this.appConfigService.updateBranding({
      colors: { ...this.appConfigService.getConfig().branding.colors, secondary: color }
    });
  }

  updateAccentColor(color: string): void {
    const root = document.documentElement;
    this.renderer.setStyle(root, '--accent-color', color);
    
    // Mettre à jour la configuration
    this.appConfigService.updateBranding({
      colors: { ...this.appConfigService.getConfig().branding.colors, accent: color }
    });
  }

  // Méthodes pour la gestion des thèmes personnalisés
  createCustomTheme(name: string, colors: Partial<ThemeColors>): void {
    const customTheme: ThemeConfig = {
      id: `custom-${Date.now()}`,
      name,
      description: 'Thème personnalisé',
      isActive: true,
      isDefault: false,
      colors: {
        primary: colors.primary || '#007bff',
        secondary: colors.secondary || '#6c757d',
        accent: colors.accent || '#17a2b8',
        success: colors.success || '#28a745',
        warning: colors.warning || '#ffc107',
        danger: colors.danger || '#dc3545',
        info: colors.info || '#17a2b8',
        light: colors.light || '#f8f9fa',
        dark: colors.dark || '#343a40',
        background: colors.background || '#ffffff',
        surface: colors.surface || '#f8f9fa',
        text: colors.text || '#212529',
        textSecondary: colors.textSecondary || '#6c757d',
        border: colors.border || '#dee2e6',
        shadow: colors.shadow || 'rgba(0, 0, 0, 0.1)'
      }
    };

    // Ajouter le thème personnalisé à la configuration
    const currentThemes = this.appConfigService.getConfigValue('theme').availableThemes;
    const updatedThemes = [...currentThemes, customTheme];
    this.appConfigService.updateTheme({ availableThemes: updatedThemes });
  }

  // Méthodes utilitaires
  isDarkTheme(): boolean {
    return this.currentThemeSubject.value === 'dark';
  }

  isLightTheme(): boolean {
    return this.currentThemeSubject.value === 'light';
  }

  isAutoTheme(): boolean {
    return this.currentThemeSubject.value === 'auto';
  }

  // Méthode pour détecter automatiquement la préférence système
  detectSystemPreference(): void {
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
      this.setTheme('dark');
    } else {
      this.setTheme('light');
    }
  }

  // Méthode pour écouter les changements de préférence système
  listenToSystemPreference(): void {
    if (window.matchMedia) {
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
      mediaQuery.addEventListener('change', (e) => {
        if (this.isAutoTheme()) {
          this.applyAutoTheme();
        }
      });
    }
  }
}
