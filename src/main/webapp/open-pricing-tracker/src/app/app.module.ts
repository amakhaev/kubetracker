import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MenuModule} from "./menu/menu.module";
import {RouterModule, Routes} from "@angular/router";
import {DashboardComponent} from "./dashboard-page/dashboard-page.component";
import {SettingsPageComponent} from "./settings-page/settings-page.component";
import {DashboardPageModule} from "./dashboard-page/dashboard-page.module";
import {SettingsPageModule} from "./settings-page/settings-page.module";
import {HttpClientModule} from "@angular/common/http";
import {OpenPricingSharedModule} from "./shared/open-pricing-shared.module";
import {FilterPageModule} from "./filter-page/filter-page.module";
import {FilterPageComponent} from "./filter-page/filter-page.component";

/**
 * List of known routes.
 */
const appRoutes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent  },
  { path: 'settings', component: SettingsPageComponent },
  { path: 'filters', component: FilterPageComponent }
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MenuModule,
    DashboardPageModule,
    SettingsPageModule,
    OpenPricingSharedModule,
    FilterPageModule
  ],
  providers: [],
  exports: [RouterModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
