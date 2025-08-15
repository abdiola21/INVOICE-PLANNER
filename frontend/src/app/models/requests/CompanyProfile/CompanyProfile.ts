export interface CompanyProfile {
  trackingId: string;
  companyName: string;
  address: string;
  city: string;
  postalCode: string;
  country: string;
  phoneNumber?: string;
  email: string;
  website?: string;
  taxNumber?: string;
  registrationNumber?: string;
  hasLogo?: boolean;
}
