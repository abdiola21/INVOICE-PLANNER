import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AppConfigService } from '../config/app-config.service';

export interface Language {
  code: string;
  name: string;
  nativeName: string;
  isActive: boolean;
  isDefault: boolean;
  flag?: string;
  direction?: 'ltr' | 'rtl';
}

export interface Translation {
  [key: string]: string | Translation | string[];
}

export interface LocalizationConfig {
  defaultLanguage: string;
  supportedLanguages: Language[];
  timezone: string;
  dateFormat: string;
  timeFormat: string;
  currency: CurrencyConfig;
}

export interface CurrencyConfig {
  code: string;
  symbol: string;
  name: string;
  decimalPlaces: number;
  thousandSeparator: string;
  decimalSeparator: string;
}

@Injectable({
  providedIn: 'root'
})
export class LocalizationService {
  private currentLanguageSubject = new BehaviorSubject<string>('fr');
  public currentLanguage$ = this.currentLanguageSubject.asObservable();

  private translations: { [lang: string]: Translation } = {};
  private currentTimezone: string = 'Africa/Abidjan';

  constructor(private appConfigService: AppConfigService) {
    this.initializeLocalization();
    this.loadTranslations();
  }

  private initializeLocalization(): void {
    // Charger la configuration de localisation
    this.appConfigService.config$.subscribe(config => {
      const localization = config.localization;
      this.currentLanguageSubject.next(localization.defaultLanguage);
      this.currentTimezone = localization.timezone;
    });

    // Charger la langue depuis le localStorage
    const savedLanguage = localStorage.getItem('selectedLanguage');
    if (savedLanguage) {
      this.setLanguage(savedLanguage);
    }
  }

  private loadTranslations(): void {
    // Charger les traductions pour le français
    this.translations['fr'] = {
      // Navigation
      'nav.features': 'Fonctionnalités',
      'nav.pricing': 'Tarifs',
      'nav.contact': 'Contact',
      'nav.login': 'Connexion',
      'nav.register': 'Inscription',

      // Hero Section
      'hero.title': 'Révolutionnez votre facturation',
      'hero.subtitle': 'Une solution intelligente qui transforme la gestion administrative en un jeu d\'enfant. Conçue pour les freelances ambitieux.',
      'hero.startButton': 'Démarrer gratuitement',
      'hero.demoButton': 'Voir la démo',
      'hero.rating': 'basé sur 1000+ avis',

      // Features
      'features.title': 'Fonctionnalités innovantes',
      'features.quotes.title': 'Devis intelligents',
      'features.quotes.description': 'Créez des devis professionnels en quelques clics avec notre éditeur intelligent et nos modèles personnalisables.',
      'features.invoicing.title': 'Facturation automatique',
      'features.invoicing.description': 'Automatisez vos factures et relances. Notre système s\'occupe de tout pendant que vous vous concentrez sur votre activité.',
      'features.analytics.title': 'Analytics avancés',
      'features.analytics.description': 'Visualisez vos performances et prenez des décisions éclairées grâce à nos tableaux de bord interactifs.',

      // Pricing
      'pricing.title': 'Des tarifs adaptés à vos besoins',
      'pricing.starter.name': 'Starter',
      'pricing.starter.features': [
        '5 devis par mois',
        '5 factures par mois',
        'Dashboard basique',
        'Support email'
      ],
      'pricing.starter.button': 'Commencer gratuitement',
      'pricing.pro.name': 'Pro',
      'pricing.pro.features': [
        'Devis illimités',
        'Factures illimitées',
        'Dashboard avancé',
        'Support prioritaire',
        'Personnalisation avancée'
      ],
      'pricing.pro.button': 'Choisir Pro',
      'pricing.popular': 'Populaire',

      // Dashboard
      'dashboard.title': 'Tableau de bord',
      'dashboard.clients': 'Clients',
      'dashboard.quotes': 'Devis',
      'dashboard.invoices': 'Factures',
      'dashboard.services': 'Services',
      'dashboard.profile': 'Mon Profil',
      'dashboard.settings': 'Paramètres',
      'dashboard.logout': 'Déconnexion',

      // Common
      'common.loading': 'Chargement...',
      'common.error': 'Erreur',
      'common.retry': 'Réessayer',
      'common.save': 'Enregistrer',
      'common.cancel': 'Annuler',
      'common.edit': 'Modifier',
      'common.delete': 'Supprimer',
      'common.add': 'Ajouter',
      'common.search': 'Rechercher',
      'common.filter': 'Filtrer',
      'common.sort': 'Trier',
      'common.actions': 'Actions',
      'common.status': 'Statut',
      'common.date': 'Date',
      'common.amount': 'Montant',
      'common.description': 'Description',
      'common.name': 'Nom',
      'common.email': 'Email',
      'common.phone': 'Téléphone',
      'common.address': 'Adresse',
      'common.company': 'Entreprise',
      'common.website': 'Site web',

      // Status
      'status.active': 'Actif',
      'status.inactive': 'Inactif',
      'status.pending': 'En attente',
      'status.approved': 'Approuvé',
      'status.rejected': 'Rejeté',
      'status.completed': 'Terminé',
      'status.cancelled': 'Annulé',
      'status.draft': 'Brouillon',
      'status.sent': 'Envoyé',
      'status.paid': 'Payé',
      'status.overdue': 'En retard',

      // Messages
      'message.success': 'Opération réussie',
      'message.error': 'Une erreur est survenue',
      'message.warning': 'Attention',
      'message.info': 'Information',
      'message.confirmDelete': 'Êtes-vous sûr de vouloir supprimer cet élément ?',
      'message.confirmAction': 'Êtes-vous sûr de vouloir effectuer cette action ?',
      'message.dataSaved': 'Données enregistrées avec succès',
      'message.dataDeleted': 'Données supprimées avec succès',
      'message.dataUpdated': 'Données mises à jour avec succès',

      // Validation
      'validation.required': 'Ce champ est requis',
      'validation.email': 'Veuillez saisir une adresse email valide',
      'validation.minLength': 'Ce champ doit contenir au moins {0} caractères',
      'validation.maxLength': 'Ce champ ne peut pas dépasser {0} caractères',
      'validation.pattern': 'Format invalide',
      'validation.unique': 'Cette valeur existe déjà',
      'validation.confirmPassword': 'Les mots de passe ne correspondent pas',

      // Time
      'time.now': 'Maintenant',
      'time.today': 'Aujourd\'hui',
      'time.yesterday': 'Hier',
      'time.tomorrow': 'Demain',
      'time.thisWeek': 'Cette semaine',
      'time.lastWeek': 'La semaine dernière',
      'time.nextWeek': 'La semaine prochaine',
      'time.thisMonth': 'Ce mois',
      'time.lastMonth': 'Le mois dernier',
      'time.nextMonth': 'Le mois prochain',
      'time.thisYear': 'Cette année',
      'time.lastYear': 'L\'année dernière',
      'time.nextYear': 'L\'année prochaine',

      // Currency
      'currency.format': '{0} {1}',
      'currency.xof': 'Franc CFA',
      'currency.eur': 'Euro',
      'currency.usd': 'Dollar US',
      'currency.gbp': 'Livre Sterling'
    };

    // Charger les traductions pour l'anglais
    this.translations['en'] = {
      // Navigation
      'nav.features': 'Features',
      'nav.pricing': 'Pricing',
      'nav.contact': 'Contact',
      'nav.login': 'Login',
      'nav.register': 'Register',

      // Hero Section
      'hero.title': 'Revolutionize your invoicing',
      'hero.subtitle': 'An intelligent solution that transforms administrative management into child\'s play. Designed for ambitious freelancers.',
      'hero.startButton': 'Start for free',
      'hero.demoButton': 'View demo',
      'hero.rating': 'based on 1000+ reviews',

      // Features
      'features.title': 'Innovative features',
      'features.quotes.title': 'Smart quotes',
      'features.quotes.description': 'Create professional quotes in a few clicks with our intelligent editor and customizable templates.',
      'features.invoicing.title': 'Automatic invoicing',
      'features.invoicing.description': 'Automate your invoices and reminders. Our system takes care of everything while you focus on your business.',
      'features.analytics.title': 'Advanced analytics',
      'features.analytics.description': 'Visualize your performance and make informed decisions with our interactive dashboards.',

      // Pricing
      'pricing.title': 'Pricing adapted to your needs',
      'pricing.starter.name': 'Starter',
      'pricing.starter.features': [
        '5 quotes per month',
        '5 invoices per month',
        'Basic dashboard',
        'Email support'
      ],
      'pricing.starter.button': 'Start for free',
      'pricing.pro.name': 'Pro',
      'pricing.pro.features': [
        'Unlimited quotes',
        'Unlimited invoices',
        'Advanced dashboard',
        'Priority support',
        'Advanced customization'
      ],
      'pricing.pro.button': 'Choose Pro',
      'pricing.popular': 'Popular',

      // Dashboard
      'dashboard.title': 'Dashboard',
      'dashboard.clients': 'Clients',
      'dashboard.quotes': 'Quotes',
      'dashboard.invoices': 'Invoices',
      'dashboard.services': 'Services',
      'dashboard.profile': 'My Profile',
      'dashboard.settings': 'Settings',
      'dashboard.logout': 'Logout',

      // Common
      'common.loading': 'Loading...',
      'common.error': 'Error',
      'common.retry': 'Retry',
      'common.save': 'Save',
      'common.cancel': 'Cancel',
      'common.edit': 'Edit',
      'common.delete': 'Delete',
      'common.add': 'Add',
      'common.search': 'Search',
      'common.filter': 'Filter',
      'common.sort': 'Sort',
      'common.actions': 'Actions',
      'common.status': 'Status',
      'common.date': 'Date',
      'common.amount': 'Amount',
      'common.description': 'Description',
      'common.name': 'Name',
      'common.email': 'Email',
      'common.phone': 'Phone',
      'common.address': 'Address',
      'common.company': 'Company',
      'common.website': 'Website',

      // Status
      'status.active': 'Active',
      'status.inactive': 'Inactive',
      'status.pending': 'Pending',
      'status.approved': 'Approved',
      'status.rejected': 'Rejected',
      'status.completed': 'Completed',
      'status.cancelled': 'Cancelled',
      'status.draft': 'Draft',
      'status.sent': 'Sent',
      'status.paid': 'Paid',
      'status.overdue': 'Overdue',

      // Messages
      'message.success': 'Operation successful',
      'message.error': 'An error occurred',
      'message.warning': 'Warning',
      'message.info': 'Information',
      'message.confirmDelete': 'Are you sure you want to delete this item?',
      'message.confirmAction': 'Are you sure you want to perform this action?',
      'message.dataSaved': 'Data saved successfully',
      'message.dataDeleted': 'Data deleted successfully',
      'message.dataUpdated': 'Data updated successfully',

      // Validation
      'validation.required': 'This field is required',
      'validation.email': 'Please enter a valid email address',
      'validation.minLength': 'This field must contain at least {0} characters',
      'validation.maxLength': 'This field cannot exceed {0} characters',
      'validation.pattern': 'Invalid format',
      'validation.unique': 'This value already exists',
      'validation.confirmPassword': 'Passwords do not match',

      // Time
      'time.now': 'Now',
      'time.today': 'Today',
      'time.yesterday': 'Yesterday',
      'time.tomorrow': 'Tomorrow',
      'time.thisWeek': 'This week',
      'time.lastWeek': 'Last week',
      'time.nextWeek': 'Next week',
      'time.thisMonth': 'This month',
      'time.lastMonth': 'Last month',
      'time.nextMonth': 'Next month',
      'time.thisYear': 'This year',
      'time.lastYear': 'Last year',
      'time.nextYear': 'Next year',

      // Currency
      'currency.format': '{0} {1}',
      'currency.xof': 'CFA Franc',
      'currency.eur': 'Euro',
      'currency.usd': 'US Dollar',
      'currency.gbp': 'Pound Sterling'
    };
  }

  getCurrentLanguage(): string {
    return this.currentLanguageSubject.value;
  }

  setLanguage(languageCode: string): void {
    if (this.isLanguageSupported(languageCode)) {
      this.currentLanguageSubject.next(languageCode);
      localStorage.setItem('selectedLanguage', languageCode);
      
      // Mettre à jour la configuration de l'application
      this.appConfigService.updateLocalization({ defaultLanguage: languageCode });
      
      // Appliquer la direction du texte si nécessaire
      this.applyTextDirection(languageCode);
    }
  }

  isLanguageSupported(languageCode: string): boolean {
    const config = this.appConfigService.getConfig();
    return config.localization.supportedLanguages.some(lang => lang.code === languageCode && lang.isActive);
  }

  getSupportedLanguages(): Language[] {
    const config = this.appConfigService.getConfig();
    return config.localization.supportedLanguages.filter(lang => lang.isActive);
  }

  getDefaultLanguage(): string {
    const config = this.appConfigService.getConfig();
    return config.localization.defaultLanguage;
  }

  // Méthodes de traduction
  translate(key: string, params?: any[]): string {
    const currentLang = this.currentLanguageSubject.value;
    const translation = this.getNestedTranslation(this.translations[currentLang], key);
    
    if (translation && typeof translation === 'string') {
      if (params && params.length > 0) {
        return this.interpolateParams(translation, params);
      }
      return translation;
    }
    
    // Retourner la clé si la traduction n'est pas trouvée
    return key;
  }

  private getNestedTranslation(obj: any, path: string): any {
    return path.split('.').reduce((current, key) => current && current[key], obj);
  }

  private interpolateParams(text: string, params: any[]): string {
    return text.replace(/\{(\d+)\}/g, (match, index) => {
      return params[index] !== undefined ? params[index] : match;
    });
  }

  // Méthodes de formatage
  formatDate(date: Date, format?: string): string {
    const currentLang = this.currentLanguageSubject.value;
    const config = this.appConfigService.getConfig();
    const dateFormat = format || config.localization.dateFormat;
    
    // Implémentation basique du formatage de date
    // Vous pouvez utiliser une bibliothèque comme date-fns ou moment.js pour plus de fonctionnalités
    return date.toLocaleDateString(currentLang === 'fr' ? 'fr-FR' : 'en-US');
  }

  formatTime(date: Date, format?: string): string {
    const currentLang = this.currentLanguageSubject.value;
    const config = this.appConfigService.getConfig();
    const timeFormat = format || config.localization.timeFormat;
    
    return date.toLocaleTimeString(currentLang === 'fr' ? 'fr-FR' : 'en-US');
  }

  formatCurrency(amount: number, currencyCode?: string): string {
    const config = this.appConfigService.getConfig();
    const currency = currencyCode || config.localization.currency.code;
    const currencyConfig = config.localization.currency;
    
    const formattedAmount = amount.toLocaleString(
      this.currentLanguageSubject.value === 'fr' ? 'fr-FR' : 'en-US',
      {
        minimumFractionDigits: currencyConfig.decimalPlaces,
        maximumFractionDigits: currencyConfig.decimalPlaces
      }
    );
    
    return this.translate('currency.format', [formattedAmount, currencyConfig.symbol]);
  }

  formatNumber(number: number, options?: Intl.NumberFormatOptions): string {
    const currentLang = this.currentLanguageSubject.value;
    const locale = currentLang === 'fr' ? 'fr-FR' : 'en-US';
    
    return number.toLocaleString(locale, options);
  }

  // Méthodes de gestion du fuseau horaire
  getCurrentTimezone(): string {
    return this.currentTimezone;
  }

  setTimezone(timezone: string): void {
    this.currentTimezone = timezone;
    this.appConfigService.updateLocalization({ timezone });
  }

  getAvailableTimezones(): string[] {
    return Intl.supportedValuesOf('timeZone');
  }

  // Méthodes de gestion de la direction du texte
  private applyTextDirection(languageCode: string): void {
    const language = this.getSupportedLanguages().find(lang => lang.code === languageCode);
    if (language && language.direction) {
      document.documentElement.setAttribute('dir', language.direction);
    } else {
      document.documentElement.removeAttribute('dir');
    }
  }

  getTextDirection(): 'ltr' | 'rtl' {
    const currentLang = this.currentLanguageSubject.value;
    const language = this.getSupportedLanguages().find(lang => lang.code === currentLang);
    return language?.direction || 'ltr';
  }

  // Méthodes utilitaires
  isRTL(): boolean {
    return this.getTextDirection() === 'rtl';
  }

  isLTR(): boolean {
    return this.getTextDirection() === 'ltr';
  }

  // Méthodes pour la gestion dynamique des langues
  addLanguage(language: Language): void {
    const currentLanguages = this.appConfigService.getConfigValue('localization').supportedLanguages;
    const updatedLanguages = [...currentLanguages, language];
    this.appConfigService.updateLocalization({ supportedLanguages: updatedLanguages });
  }

  updateLanguage(languageCode: string, updates: Partial<Language>): void {
    const currentLanguages = this.appConfigService.getConfigValue('localization').supportedLanguages;
    const updatedLanguages = currentLanguages.map(lang => 
      lang.code === languageCode ? { ...lang, ...updates } : lang
    );
    this.appConfigService.updateLocalization({ supportedLanguages: updatedLanguages });
  }

  removeLanguage(languageCode: string): void {
    const currentLanguages = this.appConfigService.getConfigValue('localization').supportedLanguages;
    const updatedLanguages = currentLanguages.filter(lang => lang.code !== languageCode);
    this.appConfigService.updateLocalization({ supportedLanguages: updatedLanguages });
  }

  // Méthodes pour la gestion des traductions
  addTranslation(languageCode: string, key: string, value: string): void {
    if (!this.translations[languageCode]) {
      this.translations[languageCode] = {};
    }
    
    const keys = key.split('.');
    let current: Translation = this.translations[languageCode];
    
    for (let i = 0; i < keys.length - 1; i++) {
      if (!current[keys[i]] || typeof current[keys[i]] === 'string' || Array.isArray(current[keys[i]])) {
        current[keys[i]] = {};
      }
      current = current[keys[i]] as Translation;
    }
    
    current[keys[keys.length - 1]] = value;
  }

  removeTranslation(languageCode: string, key: string): void {
    if (this.translations[languageCode]) {
      const keys = key.split('.');
      let current: Translation = this.translations[languageCode];
      
      for (let i = 0; i < keys.length - 1; i++) {
        if (current[keys[i]] && typeof current[keys[i]] === 'object' && !Array.isArray(current[keys[i]])) {
          current = current[keys[i]] as Translation;
        } else {
          return;
        }
      }
      
      delete current[keys[keys.length - 1]];
    }
  }
}
