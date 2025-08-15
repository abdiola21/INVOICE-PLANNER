import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListFactureComponent } from './components/list-facture/list-facture.component';
import { AddFactureComponent } from './components/add-facture/add-facture.component';
import { EditFactureComponent } from './components/edit-facture/edit-facture.component';

const routes: Routes = [
  // Liste des factures sur /dashboard/factures
  { path: '', component: ListFactureComponent },

  // Création d’une nouvelle facture sur /dashboard/factures/new
  { path: 'new', component: AddFactureComponent },

  // Édition d’une facture existante sur /dashboard/factures/edit-facture/:id
  { path: 'edit-facture/:id', component: EditFactureComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FactureRoutingModule { }

