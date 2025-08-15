import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DetailsPrestationComponent } from './components/details-prestation/details-prestation.component';
import { ListPrestationComponent } from './components/list-prestation/list-prestation.component';
import { AddPrestationComponent } from './components/add-prestation/add-prestation.component';
import { EditPrestationComponent } from './components/edit-prestation/edit-prestation.component';




const routes: Routes = [
    { path: '', component: ListPrestationComponent },
    { path: 'add-services', component: AddPrestationComponent },
    { path: 'edit/:id', component:  EditPrestationComponent},
    { path: 'details-services/:id', component:  DetailsPrestationComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PrestationsRoutingModule { }

