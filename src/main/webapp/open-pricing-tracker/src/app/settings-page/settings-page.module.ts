import {SettingsPageComponent} from "./settings-page.component";
import {NgModule} from "@angular/core";
import {FieldsetModule, InputTextModule, PasswordModule} from "primeng/primeng";
import {ButtonModule} from "primeng/button";
import {FormsModule} from "@angular/forms";

@NgModule({
  imports: [
    InputTextModule,
    PasswordModule,
    ButtonModule,
    FormsModule,
    FieldsetModule
  ],
  declarations: [
    SettingsPageComponent
  ],
  providers: [
    SettingsPageComponent
  ],
  exports: [
    SettingsPageComponent
  ],
  bootstrap: []
})
export class SettingsPageModule{}
