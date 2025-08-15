import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { PrestationsService } from '../../../../../../services/Prestations/prestations.service';
import { PrestationRequest } from '../../../../../../models/requests/Prestations/prestation-request';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-edit-prestation',
  standalone: false,
  templateUrl: './edit-prestation.component.html',
  styleUrls: ['./edit-prestation.component.css'],
})
export class EditPrestationComponent implements OnInit {
  prestationForm!: FormGroup;
  prestationId!: string;
  isLoading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private prestationsService: PrestationsService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.prestationId = this.route.snapshot.paramMap.get('id')!;

    this.prestationForm = this.fb.group({
      designation: ['', Validators.required],
      description: ['', Validators.required],
      prixUnitaire: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      duree: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
    });

    this.prestationsService
      .getPrestationById(this.prestationId)
      .subscribe((response) => {
        if (response) {
          console.log(response);
          this.prestationForm.patchValue(response);
        }
      });
  }

  onUpdate(): void {
    if (this.prestationForm.invalid) {
      return;
    }

    this.isLoading = true;
    const prestationData: PrestationRequest = this.prestationForm.value;
    prestationData.prixUnitaire =
      this.prestationForm.get('prixUnitaire')?.value;

    this.prestationsService
      .updatePrestation(this.prestationId, prestationData)
      .subscribe({
        next: () => {
          this.isLoading = false;
          Swal.fire({
            title: 'Modification réussie',
            text: 'La prestation a été modifiée avec succès',
            icon: 'success',
            draggable: true,
          });

          this.messageService.add({
            severity: 'success',
            summary: 'Modification réussie',
            detail: 'La prestation a été modifiée avec succès',
          });

          this.router.navigate(['/dashboard/prestations']);
        },
        error: (error) => {
          this.isLoading = false;
          Swal.fire({
            title: 'Erreur',
            text: "Une erreur s'est produite lors de la modification de la prestation",
            icon: 'error',
          });

          console.error('Erreur API :', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: "Une erreur s'est produite",
          });
        },
      });
  }
}

