export interface LandingData {
  hero: HeroSection;
  features: Feature[];
  pricing: PricingPlan[];
  testimonials: Testimonial[];
  footer: FooterData;
  navigation: NavigationItem[];
}

export interface HeroSection {
  title: string;
  subtitle: string;
  primaryButtonText: string;
  secondaryButtonText: string;
  primaryButtonLink: string;
  secondaryButtonLink: string;
  rating: number;
  reviewCount: number;
  reviewText: string;
  imageUrl: string;
  imageAlt: string;
}

export interface Feature {
  id: string;
  icon: string;
  title: string;
  description: string;
  order: number;
}

export interface PricingPlan {
  id: string;
  name: string;
  price: number;
  currency: string;
  period: string;
  features: string[];
  isPopular: boolean;
  popularBadgeText?: string;
  buttonText: string;
  buttonLink: string;
  order: number;
}

export interface Testimonial {
  id: string;
  name: string;
  position: string;
  company: string;
  text: string;
  avatar: string;
  rating: number;
  order: number;
}

export interface FooterData {
  logo: {
    imageUrl: string;
    imageAlt: string;
    description: string;
  };
  socialLinks: SocialLink[];
  productLinks: FooterLink[];
  resourceLinks: FooterLink[];
  newsletter: NewsletterData;
}

export interface SocialLink {
  id: string;
  platform: string;
  icon: string;
  url: string;
  order: number;
}

export interface FooterLink {
  id: string;
  text: string;
  url: string;
  order: number;
}

export interface NewsletterData {
  title: string;
  description: string;
  placeholder: string;
  buttonText: string;
}

export interface NavigationItem {
  id: string;
  text: string;
  url: string;
  order: number;
}
