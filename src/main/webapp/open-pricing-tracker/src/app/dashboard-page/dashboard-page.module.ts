import {DashboardComponent} from "./dashboard-page.component";
import {NgModule} from "@angular/core";
import {PodStatusWidgetComponent} from "./pod-status-widget/pod-status-widget.component";
import {DataViewModule} from "primeng/dataview";
import {ProgressSpinnerModule, ScrollPanelModule} from "primeng/primeng";
import {BrowserModule} from "@angular/platform-browser";
import {PodItemComponent} from "./pod-status-widget/pod-item.component";
import {JenkinsJobUiTestComponent} from "./ui-test-widget/jenkins-job-ui-test/jenkins-job-ui-test.component";
import {UiTestWidgetComponent} from "./ui-test-widget/ui-test-widget.component";
import {DatePipe} from "@angular/common";

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
    PodItemComponent,
    JenkinsJobUiTestComponent,
    UiTestWidgetComponent
  ],
  providers: [
    DashboardComponent,
    DatePipe
  ],
  exports: [
    DashboardComponent
  ],
  entryComponents: [
    PodStatusWidgetComponent,
    PodItemComponent,
    JenkinsJobUiTestComponent,
    UiTestWidgetComponent
  ],
  bootstrap: []
})
export class DashboardPageModule {}
