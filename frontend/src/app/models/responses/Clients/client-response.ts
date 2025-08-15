export interface ClientResponse {
  id?: number;                       // pour clientId côté back
  trackingId: string;

  nom: string;
  prenom?: string | null;

  email: string;
  telephone?: string | null;

  adresse?: string | null;
  ville?: string | null;
  codePostal?: string | null;
  pays?: string | null;

  // Champs souvent utilisés dans l’UI :
  societe?: string | null;

  // Suivant ce que renvoie ton API, couvre les deux écritures :
  numero_tva?: string | null;
  numeroTVA?: string | null;

  dateCreation?: Date | string | null;
  dateModification?: Date | string | null;
}

