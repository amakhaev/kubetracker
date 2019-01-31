import {SettingsService} from "./services/settings.service";
import {NgModule} from "@angular/core";
import {PodListService} from "./services/pod-list.service";
import {BrowserModule} from "@angular/platform-browser";
import {FilterService} from "./services/filter.service";

@NgModule({
  imports: [
    BrowserModule
  ],
  declarations: [
  ],
  providers: [
    SettingsService,
    PodListService,
    FilterService
  ],
  exports: [
  ],
  bootstrap: []
})
export class OpenPricingSharedModule {}
