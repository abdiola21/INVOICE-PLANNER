export interface ClientResponse {
  id?: number;              // <— AJOUT pour pouvoir passer clientId au back
  trackingId: string;

  nom: string;
  prenom?: string | null;

  email: string;
  telephone?: string | null;

  adresse?: string | null;
  ville?: string | null;
  codePostal?: string | null;
  pays?: string | null;

  // si ton API renvoie parfois ces dates manquantes, garde-les optionnelles
  dateCreation?: Date | string | null;
  dateModification?: Date | string | null;
}

