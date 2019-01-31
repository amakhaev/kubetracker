import {DashboardComponent} from "./dashboard-page.component";
import {NgModule} from "@angular/core";
import {PodStatusWidgetComponent} from "./pod-status-widget/pod-status-widget.component";
import {DataViewModule} from "primeng/dataview";
import {ProgressSpinnerModule, ScrollPanelModule} from "primeng/primeng";
import {BrowserModule} from "@angular/platform-browser";
import {PodItemComponent} from "./pod-status-widget/pod-item.component";

@NgModule({
  imports: [
    DataViewModule,
    ScrollPanelModule,
    BrowserModule,
    ProgressSpinnerModule
  ],
  declarations: [
    DashboardComponent,
    PodStatusWidgetComponent,
    PodItemComponent
  ],
  providers: [
    DashboardComponent
  ],
  exports: [
    DashboardComponent
  ],
  entryComponents: [
    PodStatusWidgetComponent,
    PodItemComponent
  ],
  bootstrap: []
})
export class DashboardPageModule {}
