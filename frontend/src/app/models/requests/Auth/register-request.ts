export interface RegisterRequest {
    prenom: string;
    nom: string;
    email: string;
    password: string;
    confirmPassword: string;
    numeroDeTelephone?: string;
}

