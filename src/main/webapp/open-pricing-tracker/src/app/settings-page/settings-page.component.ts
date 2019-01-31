import {Component, OnInit} from "@angular/core";
import {SettingsService} from "../shared/services/settings.service";
import {SettingsModel} from "../shared/models/settings.model";
import {Router} from "@angular/router";
import {environment} from "../../environments/environment";

/**
 * Provides the settings component
 */
@Component({
  selector: 'settings',
  templateUrl: './settings-page.component.html',
  styleUrls: ['./settings-page.component.scss']
})
export class SettingsPageComponent implements OnInit{

  /**
   * The settings model of application
   */
  public settings: SettingsModel;

  /**
   * Initialize new instance of SettingsService
   *
   * @param settingsService - the settings service instance
   * @param router - the Router instance
   */
  constructor(private settingsService: SettingsService, private router: Router) {
    this.settings = new SettingsModel();
  }

  /**
   * Calls lifecycle hook that is called after data-bound properties of a directive are initialized.
   */
  public ngOnInit(): void {
    this.settingsService.retrieveSettings().then(
      settings => this.settings = settings,
      error => console.error(error)
    );
  }

  /**
   * Handles the click on cancel button
   */
  public onCancelClick(): void {
    this.router.navigate([environment.navigation.dashboard]);
  }

  /**
   * Handles the click on save button
   */
  public onSaveClick(): void {
    this.settingsService.updateSettings(this.settings).then(
      updatedSettings => this.router.navigate([environment.navigation.dashboard]),
      error => console.error(error)
    );
  }
}
