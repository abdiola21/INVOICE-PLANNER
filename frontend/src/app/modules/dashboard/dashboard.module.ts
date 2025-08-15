import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DashboardRoutingModule } from './dashboard-routing.module';

// Layout Components
import { LayoutComponent } from './layout/layout.component';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { FooterComponent } from './layout/footer/footer.component';
import { BodyComponent } from './layout/body/body.component';
import { DashboardComponent } from './dashboard.component';


// Feature Components
import { HomeComponent } from '../landing/home/home.component';

// PrimeNG Modules
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { BadgeModule } from 'primeng/badge';
import { TooltipModule } from 'primeng/tooltip';
import { CheckboxModule } from 'primeng/checkbox';
import { PasswordModule } from 'primeng/password';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { CarouselModule } from 'primeng/carousel';
import { SelectModule } from 'primeng/select';

// PrimeNG Services
import { ConfirmationService } from 'primeng/api';

import { CompletionProfileComponent } from './modules/components/completion-profile/completion-profile.component';

@NgModule({
  declarations: [
    CompletionProfileComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DashboardRoutingModule,
    LayoutComponent, // composant standalone
    BodyComponent, // composant standalone
    NavbarComponent, // composant standalone
    DashboardComponent, // composant standalone
    SidebarComponent, // composant standalone
    FooterComponent, // composant standalone
    // PrimeNG Modules
    ButtonModule,
    InputTextModule,
    MenuModule,
    BadgeModule,
    TooltipModule,
    CheckboxModule,
    PasswordModule,
    MessagesModule,
    MessageModule,
    CarouselModule,
    SelectModule
  ],
  providers: [
    ConfirmationService // fournisseur pour éviter NullInjectorError
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA] // pour les éléments personnalisés
})
export class DashboardModule { }

