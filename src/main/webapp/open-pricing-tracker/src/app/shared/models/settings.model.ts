import {autoserialize} from "cerialize";
/**
 * Provides the settings model
 */
export class SettingsModel {

  @autoserialize
  public id: number;

  @autoserialize
  public kubernetesName: string;

  @autoserialize
  public jenkinsName: string;

  @autoserialize
  public password: string;

  @autoserialize
  public jenkinsApiToken: string;
}
