import {SettingsService} from "./services/settings.service";
import {NgModule} from "@angular/core";
import {PodListService} from "./services/pod-list.service";
import {BrowserModule} from "@angular/platform-browser";
import {FilterService} from "./services/filter.service";
import {JenkinsJobsService} from "./services/jenkins-jobs.service";
import {AgePipe} from "./formatter/age-pipe";

@NgModule({
  imports: [
    BrowserModule
  ],
  declarations: [
    AgePipe
  ],
  providers: [
    SettingsService,
    PodListService,
    FilterService,
    JenkinsJobsService,
    AgePipe
  ],
  exports: [
    AgePipe
  ],
  bootstrap: []
})
export class OpenPricingSharedModule {}
