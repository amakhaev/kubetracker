import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {SettingsModel} from "../models/settings.model";
import {Deserialize} from "cerialize";

/**
 * Provides the service that makes request to server for settings
 */
@Injectable()
export class SettingsService {

  /**
   * Initialize new instance of SettingsService
   *
   * @param http - the http client instance
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Retrieves the settings model from server
   */
  public retrieveSettings(): Promise<SettingsModel> {
    return new Promise((resolve, reject) => {
      this.http.get(environment.baseApiUrl + environment.urls.settings).subscribe(
        data => {
          let settings = <SettingsModel>data;
          settings = Deserialize(settings, SettingsModel);
          resolve(settings);
        },
        err => reject(err)
      );
    });
  };

  /**
   * Updates the settings of application
   *
   * @param settingsModel - the model to be updated
   * @returns {Promise<T>} update model
   */
  public updateSettings(settingsModel: SettingsModel): Promise<SettingsModel> {
    return new Promise((resolve, reject) => {
      this.http.put<SettingsModel>(environment.baseApiUrl + environment.urls.settings, settingsModel).subscribe(
        data => {
          let settings = <SettingsModel>data;
          settings = Deserialize(settings, SettingsModel);
          resolve(settings);
        },
        err => reject(err)
      );
    });
  }
}
