import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PrestationsService } from '../../../../../../services/Prestations/prestations.service';
import { MessageService } from 'primeng/api';
import Swal from 'sweetalert2';
import { PrestationRequest } from '../../../../../../models/requests/Prestations/prestation-request';

@Component({
  selector: 'app-add-prestation',
  standalone: false,
  templateUrl: './add-prestation.component.html',
  styleUrl: './add-prestation.component.css',
})
export class AddPrestationComponent implements OnInit {
  prestationForm!: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private prestationsService: PrestationsService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.initForm();

  }

  /**
   * Initialisation du formulaire avec les validations
   */
  initForm(): void {
    this.prestationForm = this.formBuilder.group({
      designation: ['', Validators.required],
      description: ['', Validators.required],
      prixUnitaire: ['', [Validators.pattern('^[0-9]+$')]],
      duree: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
    });
  }




  /**
   * Soumission du formulaire
   */
  onSubmit(): void {
    if (this.prestationForm.invalid) {
      Object.keys(this.prestationForm.controls).forEach((key) => {
        this.prestationForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const formData = this.prestationForm.value;
    const prestationData: PrestationRequest = {
      designation: formData.designation,
      description: formData.description,
      duree: formData.duree,
      prixUnitaire: formData.prixUnitaire ? formData.prixUnitaire : undefined,
      prixTotal: formData.prixTotal ? formData.prixTotal : undefined,
    };

    this.prestationsService.createPrestation(prestationData).subscribe({
      next: () => {
        this.isLoading = false;
        Swal.fire('Ajout réussi', 'La prestation a été ajoutée avec succès', 'success');
        this.messageService.add({
          severity: 'success',
          summary: 'Ajout réussi',
          detail: 'Prestation ajoutée',
        });
        this.router.navigate(['/dashboard/prestations']);
      },
      error: (error) => {
        this.isLoading = false;
        Swal.fire('Erreur', "Une erreur s'est produite", 'error');
        console.error('Erreur API :', error);
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: this.errorMessage });
      },
    });
  }
}

