import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListClientComponent } from './components/list-client/list-client.component';
import { AddClientComponent } from './components/add-client/add-client.component';
import { EditClientComponent } from './components/edit-client/edit-client.component';
import { DetailsClientComponent } from './components/details-client/details-client.component';

const routes: Routes = [
  { path: '', component: ListClientComponent },
  { path: 'add-client', component: AddClientComponent },
  { path: 'edit/:id', component: EditClientComponent },
  { path: 'details/:id', component: DetailsClientComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ClientsRoutingModule {}

