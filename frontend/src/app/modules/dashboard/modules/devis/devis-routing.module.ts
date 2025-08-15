import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListDevisComponent } from './components/list-devis/list-devis.component';
import { AddDevisComponent } from './components/add-devis/add-devis.component';
import { EditDevisComponent } from './components/edit-devis/edit-devis.component';
import { DevisComponent } from './devis.component';

const routes: Routes = [
  {
    path: '',
    component: DevisComponent,
    children: [
      { path: '', component: ListDevisComponent },
      { path: 'add-devis', component: AddDevisComponent },
      { path: 'edit-devis/:id', component: EditDevisComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DevisRoutingModule { }

