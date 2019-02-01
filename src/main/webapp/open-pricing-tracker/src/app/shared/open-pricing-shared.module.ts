import {SettingsService} from "./services/settings.service";
import {NgModule} from "@angular/core";
import {PodListService} from "./services/pod-list.service";
import {BrowserModule} from "@angular/platform-browser";
import {FilterService} from "./services/filter.service";
import {JenkinsJobsService} from "./services/jenkins-jobs.service";

@NgModule({
  imports: [
    BrowserModule
  ],
  declarations: [
  ],
  providers: [
    SettingsService,
    PodListService,
    FilterService,
    JenkinsJobsService
  ],
  exports: [
  ],
  bootstrap: []
})
export class OpenPricingSharedModule {}
