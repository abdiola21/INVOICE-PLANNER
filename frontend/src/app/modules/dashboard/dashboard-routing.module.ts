import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from '../landing/home/home.component';
import { AuthGuard } from '../../guards/auth.guard';
import { BodyComponent } from './layout/body/body.component';
import { DashboardComponent } from './dashboard.component';
import { CompletionProfileComponent } from './modules/components/completion-profile/completion-profile.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'clients',
        loadChildren: () => import('./modules/clients/clients.module').then(m => m.ClientsModule)
      },
      {
        path: 'services',
        loadChildren: () => import('./modules/prestations/prestations.module').then(m => m.PrestationsModule)
      },
      {
        path: 'devis',
        loadChildren: () => import('./modules/devis/devis.module').then(m => m.DevisModule)
      },
      {
        path: 'factures',
        loadChildren: () => import('./modules/facture/facture.module').then(m => m.FactureModule)
      },
      {
        path: 'profile',
        component: CompletionProfileComponent
      },
      {
        path: '',
        component: BodyComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }

