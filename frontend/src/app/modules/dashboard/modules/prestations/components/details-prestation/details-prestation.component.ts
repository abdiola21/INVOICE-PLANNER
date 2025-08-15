import { Component, AfterViewInit, OnInit } from '@angular/core';
import { PrestationResponse } from '../../../../../../models/responses/Prestations/prestation-response';
import { ActivatedRoute, Router } from '@angular/router';
import { PrestationsService } from '../../../../../../services/Prestations/prestations.service';
declare var bootstrap: any;
@Component({
  selector: 'app-details-prestation',
  standalone: false,
  templateUrl: './details-prestation.component.html',
  styleUrl: './details-prestation.component.css',
})
export class DetailsPrestationComponent implements OnInit {
  prestation: PrestationResponse = {} as PrestationResponse;
  prestationId!: string;
  loading: boolean = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private prestationsService: PrestationsService
  ) {}

  ngOnInit(): void {
    this.prestationId = this.route.snapshot.paramMap.get('id')!;

    this.prestationsService
      .getPrestationById(this.prestationId)
      .subscribe((response) => {
        if (response) {
          console.log(response);
          this.loading = false;
          this.error = null;
          this.prestation = response;
        }
      },
      (error) => {
        this.loading = false;
        this.error = error.message;
      }
    );
  }
}

