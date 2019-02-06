import {SettingsService} from "./services/settings.service";
import {NgModule} from "@angular/core";
import {PodListService} from "./services/pod-list.service";
import {BrowserModule} from "@angular/platform-browser";
import {FilterService} from "./services/filter.service";
import {JenkinsJobsService} from "./services/jenkins-jobs.service";
import {AgePipe} from "./formatter/age-pipe";
import {ContainerSizeComponent} from "./components/container-size/container-size.component";

@NgModule({
  imports: [
    BrowserModule
  ],
  declarations: [
    AgePipe,
    ContainerSizeComponent
  ],
  providers: [
    SettingsService,
    PodListService,
    FilterService,
    JenkinsJobsService,
    AgePipe
  ],
  exports: [
    AgePipe,
    ContainerSizeComponent
  ],
  bootstrap: []
})
export class OpenPricingSharedModule {}
