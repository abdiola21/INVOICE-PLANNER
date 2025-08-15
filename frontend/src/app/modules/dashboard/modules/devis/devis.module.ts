import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DevisRoutingModule } from './devis-routing.module';
import { ListDevisComponent } from './components/list-devis/list-devis.component';
import { AddDevisComponent } from './components/add-devis/add-devis.component';
import { EditDevisComponent } from './components/edit-devis/edit-devis.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from 'primeng/api';
import { DevisComponent } from './devis.component';

@NgModule({
  declarations: [
    DevisComponent,
    ListDevisComponent,
    AddDevisComponent,
    EditDevisComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    DevisRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ToastModule,
    ConfirmDialogModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DevisModule { }

