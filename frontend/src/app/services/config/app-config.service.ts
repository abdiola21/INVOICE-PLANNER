import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface AppConfig {
  app: AppInfo;
  branding: BrandingConfig;
  features: FeatureConfig;
  localization: LocalizationConfig;
  theme: ThemeConfig;
  api: ApiConfig;
  security: SecurityConfig;
}

export interface AppInfo {
  name: string;
  version: string;
  description: string;
  author: string;
  supportEmail: string;
  website: string;
}

export interface BrandingConfig {
  logo: {
    primary: string;
    secondary: string;
    favicon: string;
    alt: string;
  };
  colors: {
    primary: string;
    secondary: string;
    accent: string;
    success: string;
    warning: string;
    danger: string;
    info: string;
    light: string;
    dark: string;
  };
  company: {
    name: string;
    slogan: string;
    description: string;
    address: string;
    phone: string;
    email: string;
    website: string;
    socialMedia: SocialMediaConfig[];
  };
}

export interface SocialMediaConfig {
  platform: string;
  url: string;
  icon: string;
  isActive: boolean;
}

export interface FeatureConfig {
  modules: ModuleConfig[];
  permissions: PermissionConfig[];
  limits: LimitConfig;
}

export interface ModuleConfig {
  id: string;
  name: string;
  description: string;
  isEnabled: boolean;
  isVisible: boolean;
  order: number;
  icon: string;
  route: string;
  permissions: string[];
}

export interface PermissionConfig {
  id: string;
  name: string;
  description: string;
  category: string;
  isEnabled: boolean;
}

export interface LimitConfig {
  maxClients: number;
  maxInvoices: number;
  maxQuotes: number;
  maxServices: number;
  maxUsers: number;
  maxStorageGB: number;
}

export interface LocalizationConfig {
  defaultLanguage: string;
  supportedLanguages: LanguageConfig[];
  timezone: string;
  dateFormat: string;
  timeFormat: string;
  currency: CurrencyConfig;
}

export interface LanguageConfig {
  code: string;
  name: string;
  nativeName: string;
  isActive: boolean;
  isDefault: boolean;
}

export interface CurrencyConfig {
  code: string;
  symbol: string;
  name: string;
  decimalPlaces: number;
  thousandSeparator: string;
  decimalSeparator: string;
}

export interface ThemeConfig {
  defaultTheme: 'light' | 'dark' | 'auto';
  availableThemes: ThemeOption[];
  customColors: boolean;
  colorScheme: 'default' | 'custom';
}

export interface ThemeOption {
  id: string;
  name: string;
  description: string;
  isActive: boolean;
  isDefault: boolean;
}

export interface ApiConfig {
  baseUrl: string;
  version: string;
  timeout: number;
  retryAttempts: number;
  endpoints: EndpointConfig[];
}

export interface EndpointConfig {
  name: string;
  path: string;
  method: string;
  isActive: boolean;
}

export interface SecurityConfig {
  sessionTimeout: number;
  maxLoginAttempts: number;
  passwordPolicy: PasswordPolicy;
  twoFactorAuth: TwoFactorConfig;
  ssl: boolean;
}

export interface PasswordPolicy {
  minLength: number;
  requireUppercase: boolean;
  requireLowercase: boolean;
  requireNumbers: boolean;
  requireSpecialChars: boolean;
  maxAge: number;
}

export interface TwoFactorConfig {
  isEnabled: boolean;
  methods: string[];
  isRequired: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AppConfigService {
  private configSubject = new BehaviorSubject<AppConfig>(this.getDefaultConfig());
  public config$ = this.configSubject.asObservable();

  constructor() {
    this.loadConfig();
  }

  private getDefaultConfig(): AppConfig {
    return {
      app: {
        name: 'InvoicePlanner',
        version: '1.0.0',
        description: 'Solution intelligente de gestion de facturation pour freelances',
        author: 'InvoicePlanner Team',
        supportEmail: 'support@invoiceplanner.com',
        website: 'https://invoiceplanner.com'
      },
      branding: {
        logo: {
          primary: 'assets/images/logo/invoiceslogan.png',
          secondary: 'assets/images/removebg.png',
          favicon: 'assets/images/favicon.ico',
          alt: 'InvoicePlanner Logo'
        },
        colors: {
          primary: '#007bff',
          secondary: '#6c757d',
          accent: '#17a2b8',
          success: '#28a745',
          warning: '#ffc107',
          danger: '#dc3545',
          info: '#17a2b8',
          light: '#f8f9fa',
          dark: '#343a40'
        },
        company: {
          name: 'InvoicePlanner',
          slogan: 'Révolutionnez votre facturation',
          description: 'Simplifiez votre gestion administrative et concentrez-vous sur ce qui compte vraiment : développer votre activité.',
          address: 'Abidjan, Côte d\'Ivoire',
          phone: '+225 0123456789',
          email: 'contact@invoiceplanner.com',
          website: 'https://invoiceplanner.com',
          socialMedia: [
            {
              platform: 'LinkedIn',
              url: 'https://linkedin.com/company/invoiceplanner',
              icon: 'ph-duotone ph-linkedin-logo',
              isActive: true
            },
            {
              platform: 'Twitter',
              url: 'https://twitter.com/invoiceplanner',
              icon: 'ph-duotone ph-twitter-logo',
              isActive: true
            },
            {
              platform: 'Facebook',
              url: 'https://facebook.com/invoiceplanner',
              icon: 'ph-duotone ph-facebook-logo',
              isActive: true
            }
          ]
        }
      },
      features: {
        modules: [
          {
            id: 'dashboard',
            name: 'Tableau de bord',
            description: 'Vue d\'ensemble de votre activité',
            isEnabled: true,
            isVisible: true,
            order: 1,
            icon: 'bx bxs-dashboard',
            route: '/dashboard',
            permissions: ['VIEW_DASHBOARD']
          },
          {
            id: 'clients',
            name: 'Gestion des clients',
            description: 'Gérez vos clients et prospects',
            isEnabled: true,
            isVisible: true,
            order: 2,
            icon: 'bx bx-user',
            route: '/dashboard/clients',
            permissions: ['VIEW_CLIENTS', 'CREATE_CLIENTS', 'EDIT_CLIENTS', 'DELETE_CLIENTS']
          },
          {
            id: 'devis',
            name: 'Gestion des devis',
            description: 'Créez et gérez vos devis',
            isEnabled: true,
            isVisible: true,
            order: 3,
            icon: 'ph-duotone ph-file-text',
            route: '/dashboard/devis',
            permissions: ['VIEW_QUOTES', 'CREATE_QUOTES', 'EDIT_QUOTES', 'DELETE_QUOTES']
          },
          {
            id: 'factures',
            name: 'Gestion des factures',
            description: 'Créez et suivez vos factures',
            isEnabled: true,
            isVisible: true,
            order: 4,
            icon: 'ph-duotone ph-receipt',
            route: '/dashboard/factures',
            permissions: ['VIEW_INVOICES', 'CREATE_INVOICES', 'EDIT_INVOICES', 'DELETE_INVOICES']
          },
          {
            id: 'services',
            name: 'Gestion des services',
            description: 'Définissez vos services et tarifs',
            isEnabled: true,
            isVisible: true,
            order: 5,
            icon: 'bx bx-shopping-bag',
            route: '/dashboard/services',
            permissions: ['VIEW_SERVICES', 'CREATE_SERVICES', 'EDIT_SERVICES', 'DELETE_SERVICES']
          }
        ],
        permissions: [
          {
            id: 'VIEW_DASHBOARD',
            name: 'Voir le tableau de bord',
            description: 'Accès au tableau de bord principal',
            category: 'dashboard',
            isEnabled: true
          },
          {
            id: 'VIEW_CLIENTS',
            name: 'Voir les clients',
            description: 'Accès à la liste des clients',
            category: 'clients',
            isEnabled: true
          },
          {
            id: 'CREATE_CLIENTS',
            name: 'Créer des clients',
            description: 'Créer de nouveaux clients',
            category: 'clients',
            isEnabled: true
          },
          {
            id: 'EDIT_CLIENTS',
            name: 'Modifier les clients',
            description: 'Modifier les informations des clients',
            category: 'clients',
            isEnabled: true
          },
          {
            id: 'DELETE_CLIENTS',
            name: 'Supprimer les clients',
            description: 'Supprimer des clients',
            category: 'clients',
            isEnabled: true
          }
        ],
        limits: {
          maxClients: 1000,
          maxInvoices: 10000,
          maxQuotes: 10000,
          maxServices: 500,
          maxUsers: 50,
          maxStorageGB: 10
        }
      },
      localization: {
        defaultLanguage: 'fr',
        supportedLanguages: [
          {
            code: 'fr',
            name: 'Français',
            nativeName: 'Français',
            isActive: true,
            isDefault: true
          },
          {
            code: 'en',
            name: 'English',
            nativeName: 'English',
            isActive: true,
            isDefault: false
          }
        ],
        timezone: 'Africa/Abidjan',
        dateFormat: 'DD/MM/YYYY',
        timeFormat: 'HH:mm',
        currency: {
          code: 'XOF',
          symbol: 'Fcfa',
          name: 'Franc CFA',
          decimalPlaces: 0,
          thousandSeparator: ' ',
          decimalSeparator: ','
        }
      },
      theme: {
        defaultTheme: 'light',
        availableThemes: [
          {
            id: 'light',
            name: 'Clair',
            description: 'Thème clair par défaut',
            isActive: true,
            isDefault: true
          },
          {
            id: 'dark',
            name: 'Sombre',
            description: 'Thème sombre',
            isActive: true,
            isDefault: false
          },
          {
            id: 'auto',
            name: 'Automatique',
            description: 'Thème adaptatif selon l\'heure',
            isActive: true,
            isDefault: false
          }
        ],
        customColors: false,
        colorScheme: 'default'
      },
      api: {
        baseUrl: 'https://api.invoiceplanner.com',
        version: 'v1',
        timeout: 30000,
        retryAttempts: 3,
        endpoints: [
          {
            name: 'auth',
            path: '/auth',
            method: 'POST',
            isActive: true
          },
          {
            name: 'users',
            path: '/users',
            method: 'GET',
            isActive: true
          },
          {
            name: 'clients',
            path: '/clients',
            method: 'GET',
            isActive: true
          }
        ]
      },
      security: {
        sessionTimeout: 3600000, // 1 heure
        maxLoginAttempts: 5,
        passwordPolicy: {
          minLength: 8,
          requireUppercase: true,
          requireLowercase: true,
          requireNumbers: true,
          requireSpecialChars: true,
          maxAge: 90 // jours
        },
        twoFactorAuth: {
          isEnabled: false,
          methods: ['sms', 'email', 'app'],
          isRequired: false
        },
        ssl: true
      }
    };
  }

  private loadConfig(): void {
    // Ici, vous pouvez charger la configuration depuis une API ou un fichier
    // Pour l'instant, on utilise la configuration par défaut
    const savedConfig = localStorage.getItem('appConfig');
    if (savedConfig) {
      try {
        const parsedConfig = JSON.parse(savedConfig);
        this.configSubject.next({ ...this.getDefaultConfig(), ...parsedConfig });
      } catch (error) {
        console.error('Erreur lors du chargement de la configuration:', error);
      }
    }
  }

  getConfig(): AppConfig {
    return this.configSubject.value;
  }

  getConfigValue<K extends keyof AppConfig>(key: K): AppConfig[K] {
    return this.configSubject.value[key];
  }

  updateConfig(updates: Partial<AppConfig>): void {
    const currentConfig = this.configSubject.value;
    const newConfig = { ...currentConfig, ...updates };
    this.configSubject.next(newConfig);
    
    // Sauvegarder dans le localStorage
    localStorage.setItem('appConfig', JSON.stringify(newConfig));
  }

  updateBranding(branding: Partial<BrandingConfig>): void {
    const currentConfig = this.configSubject.value;
    const newBranding = { ...currentConfig.branding, ...branding };
    this.updateConfig({ branding: newBranding });
  }

  updateFeatures(features: Partial<FeatureConfig>): void {
    const currentConfig = this.configSubject.value;
    const newFeatures = { ...currentConfig.features, ...features };
    this.updateConfig({ features: newFeatures });
  }

  updateLocalization(localization: Partial<LocalizationConfig>): void {
    const currentConfig = this.configSubject.value;
    const newLocalization = { ...currentConfig.localization, ...localization };
    this.updateConfig({ localization: newLocalization });
  }

  updateTheme(theme: Partial<ThemeConfig>): void {
    const currentConfig = this.configSubject.value;
    const newTheme = { ...currentConfig.theme, ...theme };
    this.updateConfig({ theme: newTheme });
  }

  isFeatureEnabled(featureId: string): boolean {
    const config = this.configSubject.value;
    const feature = config.features.modules.find(m => m.id === featureId);
    return feature ? feature.isEnabled : false;
  }

  hasPermission(permissionId: string): boolean {
    const config = this.configSubject.value;
    const permission = config.features.permissions.find(p => p.id === permissionId);
    return permission ? permission.isEnabled : false;
  }

  getModuleConfig(moduleId: string): ModuleConfig | undefined {
    const config = this.configSubject.value;
    return config.features.modules.find(m => m.id === moduleId);
  }

  getActiveModules(): ModuleConfig[] {
    const config = this.configSubject.value;
    return config.features.modules
      .filter(m => m.isEnabled && m.isVisible)
      .sort((a, b) => a.order - b.order);
  }

  getSupportedLanguages(): LanguageConfig[] {
    const config = this.configSubject.value;
    return config.localization.supportedLanguages.filter(l => l.isActive);
  }

  getAvailableThemes(): ThemeOption[] {
    const config = this.configSubject.value;
    return config.theme.availableThemes.filter(t => t.isActive);
  }

  resetToDefault(): void {
    const defaultConfig = this.getDefaultConfig();
    this.configSubject.next(defaultConfig);
    localStorage.removeItem('appConfig');
  }
}
