import {autoserialize} from "cerialize";
/**
 * Provides the settings model
 */
export class SettingsModel {

  @autoserialize
  public id: number;

  @autoserialize
  public fullName: string;

  @autoserialize
  public shortName: string;

  @autoserialize
  public password: string;
}
