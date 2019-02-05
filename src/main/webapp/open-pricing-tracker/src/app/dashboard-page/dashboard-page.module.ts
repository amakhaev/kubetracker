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
import {BuildsWidgetComponent} from "./builds-widget/builds-widget.component";
import {AgePipe} from "../shared/formatter/age-pipe";
import {OpenPricingSharedModule} from "../shared/open-pricing-shared.module";
import {BuildItemComponent} from "./builds-widget/build-item.component";

@NgModule({
  imports: [
    DataViewModule,
    ScrollPanelModule,
    BrowserModule,
    ProgressSpinnerModule,
    OpenPricingSharedModule
  ],
  declarations: [
    DashboardComponent,
    PodStatusWidgetComponent,
    PodItemComponent,
    JenkinsJobUiTestComponent,
    UiTestWidgetComponent,
    BuildsWidgetComponent,
    BuildItemComponent
  ],
  providers: [
    DashboardComponent,
    DatePipe,
    AgePipe
  ],
  exports: [
    DashboardComponent
  ],
  entryComponents: [
    PodStatusWidgetComponent,
    PodItemComponent,
    JenkinsJobUiTestComponent,
    UiTestWidgetComponent,
    BuildsWidgetComponent,
    BuildItemComponent
  ],
  bootstrap: []
})
export class DashboardPageModule {}
