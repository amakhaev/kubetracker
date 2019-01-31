import {SidebarModule} from "primeng/primeng";
import {NgModule} from "@angular/core";
import {MenuComponent} from "./menu.component";

@NgModule({
  imports: [
    SidebarModule
  ],
  declarations: [
    MenuComponent
  ],
  providers: [
    MenuComponent
  ],
  exports: [
    MenuComponent
  ],
  bootstrap: []
})
export class MenuModule{}
