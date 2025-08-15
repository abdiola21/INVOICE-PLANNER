import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PrestationsRoutingModule } from './prestations-routing.module';
import { PrestationsComponent } from './prestations.component';
import { AddPrestationComponent } from './components/add-prestation/add-prestation.component';
import { ListPrestationComponent } from './components/list-prestation/list-prestation.component';
import { EditPrestationComponent } from './components/edit-prestation/edit-prestation.component';  
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { DetailsPrestationComponent } from './components/details-prestation/details-prestation.component';
import { SharedModule } from '../../../../shared/shared.module';

@NgModule({
  declarations: [
    PrestationsComponent,
    AddPrestationComponent,
    ListPrestationComponent,
    EditPrestationComponent,
    DetailsPrestationComponent
  ],
  imports: [
    CommonModule,
    PrestationsRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    SharedModule
  ]
})
export class PrestationsModule { }

