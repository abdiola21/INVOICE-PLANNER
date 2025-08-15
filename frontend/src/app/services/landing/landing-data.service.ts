import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { LandingData, HeroSection, Feature, PricingPlan, Testimonial, FooterData, NavigationItem } from '../../models/landing-data';

@Injectable({
  providedIn: 'root'
})
export class LandingDataService {
  private landingData: LandingData = {
    hero: {
      title: 'Révolutionnez votre facturation',
      subtitle: 'Une solution intelligente qui transforme la gestion administrative en un jeu d\'enfant. Conçue pour les freelances ambitieux.',
      primaryButtonText: 'Démarrer gratuitement',
      secondaryButtonText: 'Voir la démo',
      primaryButtonLink: '/auth',
      secondaryButtonLink: '#demo',
      rating: 4.9,
      reviewCount: 1000,
      reviewText: 'basé sur 1000+ avis',
      imageUrl: 'assets/images/header-main.jpg',
      imageAlt: 'Dashboard Preview'
    },
    features: [
      {
        id: '1',
        icon: 'ph-duotone ph-file-text',
        title: 'Devis intelligents',
        description: 'Créez des devis professionnels en quelques clics avec notre éditeur intelligent et nos modèles personnalisables.',
        order: 1
      },
      {
        id: '2',
        icon: 'ph-duotone ph-receipt',
        title: 'Facturation automatique',
        description: 'Automatisez vos factures et relances. Notre système s\'occupe de tout pendant que vous vous concentrez sur votre activité.',
        order: 2
      },
      {
        id: '3',
        icon: 'ph-duotone ph-chart-line',
        title: 'Analytics avancés',
        description: 'Visualisez vos performances et prenez des décisions éclairées grâce à nos tableaux de bord interactifs.',
        order: 3
      }
    ],
    pricing: [
      {
        id: '1',
        name: 'Starter',
        price: 0,
        currency: 'Fcfa',
        period: '/mois',
        features: [
          '5 devis par mois',
          '5 factures par mois',
          'Dashboard basique',
          'Support email'
        ],
        isPopular: false,
        buttonText: 'Commencer gratuitement',
        buttonLink: '/auth/register',
        order: 1
      },
      {
        id: '2',
        name: 'Pro',
        price: 29,
        currency: 'Fcfa',
        period: '/mois',
        features: [
          'Devis illimités',
          'Factures illimitées',
          'Dashboard avancé',
          'Support prioritaire',
          'Personnalisation avancée'
        ],
        isPopular: true,
        popularBadgeText: 'Populaire',
        buttonText: 'Choisir Pro',
        buttonLink: '/auth/register',
        order: 2
      }
    ],
    testimonials: [
      {
        id: '1',
        name: 'Sophie Martin',
        position: 'Directrice',
        company: 'Design Studio',
        text: 'InvoicePlanner a complètement transformé notre processus de facturation. Nous économisons des heures chaque semaine et nos clients sont ravis de la qualité professionnelle de nos factures.',
        avatar: 'https://i.pravatar.cc/150?img=1',
        rating: 5,
        order: 1
      },
      {
        id: '2',
        name: 'Thomas Dubois',
        position: 'Fondateur',
        company: 'Agence Web',
        text: 'Après avoir essayé plusieurs solutions, InvoicePlanner est de loin la plus intuitive et complète. La possibilité de convertir les devis en factures en un clic est un vrai gain de temps.',
        avatar: 'https://i.pravatar.cc/150?img=2',
        rating: 5,
        order: 2
      },
      {
        id: '3',
        name: 'Marie Leroy',
        position: 'Comptable',
        company: 'Cabinet Conseil',
        text: 'Les rapports détaillés et la gestion des paiements en ligne ont considérablement amélioré notre trésorerie. Je recommande vivement InvoicePlanner à toutes les entreprises.',
        avatar: 'https://i.pravatar.cc/150?img=3',
        rating: 5,
        order: 3
      }
    ],
    footer: {
      logo: {
        imageUrl: 'assets/images/removebg.png',
        imageAlt: 'InvoicePlanner Logo',
        description: 'Simplifiez votre gestion administrative et concentrez-vous sur ce qui compte vraiment : développer votre activité.'
      },
      socialLinks: [
        {
          id: '1',
          platform: 'LinkedIn',
          icon: 'ph-duotone ph-linkedin-logo',
          url: '#',
          order: 1
        },
        {
          id: '2',
          platform: 'Twitter',
          icon: 'ph-duotone ph-twitter-logo',
          url: '#',
          order: 2
        },
        {
          id: '3',
          platform: 'Facebook',
          icon: 'ph-duotone ph-facebook-logo',
          url: '#',
          order: 3
        }
      ],
      productLinks: [
        {
          id: '1',
          text: 'Fonctionnalités',
          url: '#',
          order: 1
        },
        {
          id: '2',
          text: 'Tarifs',
          url: '#',
          order: 2
        },
        {
          id: '3',
          text: 'FAQ',
          url: '#',
          order: 3
        }
      ],
      resourceLinks: [
        {
          id: '1',
          text: 'Blog',
          url: '#',
          order: 1
        },
        {
          id: '2',
          text: 'Documentation',
          url: '#',
          order: 2
        },
        {
          id: '3',
          text: 'Support',
          url: '#',
          order: 3
        }
      ],
      newsletter: {
        title: 'Newsletter',
        description: 'Restez informé des dernières nouveautés et conseils pour freelances',
        placeholder: 'Votre email',
        buttonText: 'S\'inscrire'
      }
    },
    navigation: [
      {
        id: '1',
        text: 'Fonctionnalités',
        url: '#fonctionnalites',
        order: 1
      },
      {
        id: '2',
        text: 'Tarifs',
        url: '#tarifs',
        order: 2
      },
      {
        id: '3',
        text: 'Contact',
        url: '#contact',
        order: 3
      }
    ]
  };

  constructor() { }

  getLandingData(): Observable<LandingData> {
    return of(this.landingData);
  }

  getHeroSection(): Observable<HeroSection> {
    return of(this.landingData.hero);
  }

  getFeatures(): Observable<Feature[]> {
    return of(this.landingData.features.sort((a, b) => a.order - b.order));
  }

  getPricingPlans(): Observable<PricingPlan[]> {
    return of(this.landingData.pricing.sort((a, b) => a.order - b.order));
  }

  getTestimonials(): Observable<Testimonial[]> {
    return of(this.landingData.testimonials.sort((a, b) => a.order - b.order));
  }

  getFooterData(): Observable<FooterData> {
    return of(this.landingData.footer);
  }

  getNavigationItems(): Observable<NavigationItem[]> {
    return of(this.landingData.navigation.sort((a, b) => a.order - b.order));
  }

  // Méthodes pour mettre à jour dynamiquement les données
  updateHeroSection(hero: Partial<HeroSection>): void {
    this.landingData.hero = { ...this.landingData.hero, ...hero };
  }

  addFeature(feature: Feature): void {
    this.landingData.features.push(feature);
    this.landingData.features.sort((a, b) => a.order - b.order);
  }

  updateFeature(id: string, updates: Partial<Feature>): void {
    const index = this.landingData.features.findIndex(f => f.id === id);
    if (index !== -1) {
      this.landingData.features[index] = { ...this.landingData.features[index], ...updates };
    }
  }

  removeFeature(id: string): void {
    this.landingData.features = this.landingData.features.filter(f => f.id !== id);
  }

  addPricingPlan(plan: PricingPlan): void {
    this.landingData.pricing.push(plan);
    this.landingData.pricing.sort((a, b) => a.order - b.order);
  }

  updatePricingPlan(id: string, updates: Partial<PricingPlan>): void {
    const index = this.landingData.pricing.findIndex(p => p.id === id);
    if (index !== -1) {
      this.landingData.pricing[index] = { ...this.landingData.pricing[index], ...updates };
    }
  }

  removePricingPlan(id: string): void {
    this.landingData.pricing = this.landingData.pricing.filter(p => p.id !== id);
  }

  addTestimonial(testimonial: Testimonial): void {
    this.landingData.testimonials.push(testimonial);
    this.landingData.testimonials.sort((a, b) => a.order - b.order);
  }

  updateTestimonial(id: string, updates: Partial<Testimonial>): void {
    const index = this.landingData.testimonials.findIndex(t => t.id === id);
    if (index !== -1) {
      this.landingData.testimonials[index] = { ...this.landingData.testimonials[index], ...updates };
    }
  }

  removeTestimonial(id: string): void {
    this.landingData.testimonials = this.landingData.testimonials.filter(t => t.id !== id);
  }
}
