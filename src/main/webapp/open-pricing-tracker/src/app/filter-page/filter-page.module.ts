import {NgModule} from "@angular/core";
import {FilterPageComponent} from "./filter-page.component";
import {InputTextModule} from "primeng/primeng";
import {ButtonModule} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {BrowserModule} from "@angular/platform-browser";

@NgModule({
  imports: [
    BrowserModule,
    InputTextModule,
    ButtonModule,
    FormsModule
  ],
  declarations: [
    FilterPageComponent
  ],
  providers: [
    FilterPageComponent
  ],
  exports: [
    FilterPageComponent
  ],
  bootstrap: []
})
export class FilterPageModule {}
