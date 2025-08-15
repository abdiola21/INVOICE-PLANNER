import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

// PrimeNG Components
import { ButtonModule } from 'primeng/button';
import { CarouselModule } from 'primeng/carousel';

// Services
import { LandingDataService } from '../../../services/landing/landing-data.service';
import { AppConfigService } from '../../../services/config/app-config.service';

// Models
import { LandingData, HeroSection, Feature, PricingPlan, Testimonial, FooterData, NavigationItem } from '../../../models/landing-data';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: true,
  imports: [CommonModule, ButtonModule, CarouselModule]
})
export class HomeComponent implements OnInit {
  // Données dynamiques
  hero: HeroSection | null = null;
  features: Feature[] = [];
  pricing: PricingPlan[] = [];
  testimonials: Testimonial[] = [];
  footer: FooterData | null = null;
  navigation: NavigationItem[] = [];
  
  // Configuration de l'application
  appConfig: any = null;
  
  // États de chargement
  isLoading = true;
  error: string | null = null;

  constructor(
    private router: Router,
    private landingDataService: LandingDataService,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit(): void {
    this.loadAllData();
  }

  private loadAllData(): void {
    this.isLoading = true;
    this.error = null;

    // Charger la configuration de l'application
    this.appConfigService.config$.subscribe(config => {
      this.appConfig = config;
    });

    // Charger toutes les données de la page d'accueil
    Promise.all([
      this.landingDataService.getHeroSection().toPromise(),
      this.landingDataService.getFeatures().toPromise(),
      this.landingDataService.getPricingPlans().toPromise(),
      this.landingDataService.getTestimonials().toPromise(),
      this.landingDataService.getFooterData().toPromise(),
      this.landingDataService.getNavigationItems().toPromise()
    ]).then(([hero, features, pricing, testimonials, footer, navigation]) => {
      this.hero = hero || null;
      this.features = features || [];
      this.pricing = pricing || [];
      this.testimonials = testimonials || [];
      this.footer = footer || null;
      this.navigation = navigation || [];
      
      this.isLoading = false;
    }).catch(error => {
      console.error('Erreur lors du chargement des données:', error);
      this.error = 'Erreur lors du chargement des données';
      this.isLoading = false;
    });
  }

  // Méthodes de navigation
  navigateToRegister(): void {
    this.router.navigate(['/auth/register']);
  }

  navigateToLogin(): void {
    this.router.navigate(['/auth']);
  }

  navigateToSection(sectionId: string): void {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  // Méthodes utilitaires
  generateStars(rating: number): number[] {
    return Array.from({ length: Math.floor(rating) }, (_, i) => i);
  }

  hasRemainingStars(rating: number): boolean {
    return rating % 1 !== 0;
  }

  getRemainingStars(rating: number): number[] {
    const remaining = Math.ceil(rating) - Math.floor(rating);
    return remaining > 0 ? Array.from({ length: 1 }, (_, i) => i) : [];
  }

  // Méthodes pour la gestion dynamique des données
  refreshData(): void {
    this.loadAllData();
  }

  // Méthodes pour les actions utilisateur
  onNewsletterSubscribe(email: string): void {
    // Implémenter la logique d'inscription à la newsletter
    console.log('Inscription newsletter:', email);
    // Ici vous pouvez appeler un service pour sauvegarder l'email
  }

  onSocialMediaClick(platform: string, url: string): void {
    // Implémenter la logique de suivi des clics sur les réseaux sociaux
    console.log('Clic sur', platform, ':', url);
    window.open(url, '_blank');
  }

  // Méthodes pour la personnalisation dynamique
  updateHeroTitle(newTitle: string): void {
    if (this.hero) {
      this.landingDataService.updateHeroSection({ title: newTitle });
      this.hero.title = newTitle;
    }
  }

  addNewFeature(feature: Feature): void {
    this.landingDataService.addFeature(feature);
    this.features.push(feature);
    this.features.sort((a, b) => a.order - b.order);
  }

  addNewPricingPlan(plan: PricingPlan): void {
    this.landingDataService.addPricingPlan(plan);
    this.pricing.push(plan);
    this.pricing.sort((a, b) => a.order - b.order);
  }

  addNewTestimonial(testimonial: Testimonial): void {
    this.landingDataService.addTestimonial(testimonial);
    this.testimonials.push(testimonial);
    this.testimonials.sort((a, b) => a.order - b.order);
  }
} 
