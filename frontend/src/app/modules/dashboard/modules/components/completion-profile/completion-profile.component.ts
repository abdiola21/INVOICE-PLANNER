import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CompletionProfileService } from '../../../../../services/completionProfile/completion-profile.service';
import { CompanyProfile } from '../../../../../models/requests/CompanyProfile/CompanyProfile';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-completion-profile',
  standalone: false,
  templateUrl: './completion-profile.component.html',
  styleUrl: './completion-profile.component.css'
})
export class CompletionProfileComponent implements OnInit {
  profileForm: FormGroup;
  isProfileComplete = false;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  profile: CompanyProfile | null = null;
  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;

  constructor(
    private fb: FormBuilder,
    private profileService: CompletionProfileService
  ) {
    this.profileForm = this.fb.group({
      companyName: ['', Validators.required],
      address: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', Validators.required],
      country: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      website: [''],
      taxNumber: [''],
      registrationNumber: ['']
    });
  }

  ngOnInit(): void {
    console.log('Initialisation de CompletionProfileComponent');
    this.checkProfileStatus();
    this.loadProfile();
  }

  checkProfileStatus(): void {
    console.log('Vérification du statut du profil...');
    this.isLoading = true;
    this.profileService.isProfileComplete()
      .pipe(
        catchError(err => {
          console.error('Erreur lors de la vérification du statut:', err);
          this.errorMessage = 'Erreur lors de la vérification du statut du profil';
          return of({ success: false, message: err.message, data: false });
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('Fin de la vérification du statut. isProfileComplete =', this.isProfileComplete);
        })
      )
      .subscribe(response => {
        console.log('Réponse statut profil:', response);
        this.isProfileComplete = response.data;
      });
  }

  loadProfile(): void {
    console.log('Chargement du profil...');
    this.isLoading = true;
    this.profileService.getProfile()
      .pipe(
        catchError(err => {
          console.error('Erreur lors du chargement du profil:', err);
          console.error('Status:', err.status);
          console.error('Message:', err.message);
          console.error('Error body:', err.error);
          
          if (err.status !== 404) {
            this.errorMessage = 'Erreur lors du chargement du profil';
          }
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('Fin du chargement du profil');
        })
      )
      .subscribe(profile => {
        console.log('Profil récupéré:', profile);
        if (profile) {
          this.profile = profile;
          this.profileForm.patchValue({
            companyName: profile.companyName,
            address: profile.address,
            city: profile.city,
            postalCode: profile.postalCode,
            country: profile.country,
            phoneNumber: profile.phoneNumber,
            email: profile.email,
            website: profile.website,
            taxNumber: profile.taxNumber,
            registrationNumber: profile.registrationNumber
          });
          if (profile.trackingId && profile.hasLogo) {
            console.log('Le profil a un logo, mise à jour de l\'URL de prévisualisation');
            this.updatePreviewUrl(profile.trackingId);
          } else {
            console.log('Le profil n\'a pas de logo ou pas de trackingId');
            console.log('trackingId:', profile.trackingId);
            console.log('hasLogo:', profile.hasLogo);
          }
        }
      });
  }

  onSubmit(): void {
    console.log('Soumission du formulaire');
    if (this.profileForm.invalid) {
      console.warn('Formulaire invalide', this.profileForm.value);
      this.profileForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';
    const profileData = this.profileForm.value;
    console.log('Données à envoyer pour création/mise à jour:', profileData);

    this.profileService.createOrUpdateProfile(profileData)
      .pipe(
        catchError(err => {
          console.error('Erreur lors de l\'enregistrement du profil:', err);
          this.errorMessage = 'Erreur lors de l\'enregistrement du profil';
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('Fin de l\'enregistrement du profil');
        })
      )
      .subscribe(response => {
        console.log('Réponse createOrUpdateProfile:', response);
        if (response) {
          this.profile = response;
          this.successMessage = 'Profil enregistré avec succès';
          this.checkProfileStatus();

          if (this.selectedFile) {
            this.uploadLogo();
          }
        }
      });
  }

  onFileChange(event: any): void {
    console.log('Événement de sélection de fichier:', event);
    if (event.target.files?.length) {
      this.selectedFile = event.target.files[0];
      console.log('Fichier sélectionné:', this.selectedFile);
      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
        console.log('Preview loaded:', this.previewUrl);
      };
      reader.readAsDataURL(this.selectedFile!);
    }
  }

  uploadLogo(): void {
    console.log('Début du téléversement du logo pour le profil', this.profile);
    if (!this.selectedFile) {
      console.warn('Aucun fichier sélectionné pour le téléversement du logo');
      return;
    }

    this.isLoading = true;
    this.profileService.uploadLogo(this.selectedFile)
      .pipe(
        catchError(err => {
          console.error('Erreur lors du téléchargement du logo:', err);
          this.errorMessage = 'Erreur lors du téléchargement du logo';
          return of(null);
        }),
        finalize(() => {
          this.isLoading = false;
          console.log('Fin du téléversement du logo');
        })
      )
      .subscribe(response => {
        console.log('Réponse uploadLogo:', response);
        if (response) {
          this.profile = response;
          if (this.profile) {
            this.profile.hasLogo = true;
          }
          this.successMessage = 'Logo téléchargé avec succès';
          if (response.trackingId) {
            this.updatePreviewUrl(response.trackingId);
          }
        }
      });
  }

  /**
   * Met à jour l'URL de prévisualisation du logo en ajoutant un timestamp
   * pour éviter la mise en cache du navigateur.
   */
  private updatePreviewUrl(profileId: string): void {
    const timestamp = new Date().getTime();
    this.profileService.getLogoImage(profileId)
      .pipe(
        catchError(err => {
          console.error('Erreur lors du chargement du logo:', err);
          return of(null);
        })
      )
      .subscribe(blob => {
        if (blob) {
          this.previewUrl = URL.createObjectURL(blob);
          console.log('Preview logo URL (cache bust):', this.previewUrl);
        }
      });
  }
}

