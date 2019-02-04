import {autoserialize, autoserializeAs} from "cerialize";
/**
 * Provides the model that described jenkins job
 */
export class JenkinsJobModel {

  public dateNow: number = Date.now();

  public dateStartedAt: Date;
  public _startedAt: number;

  @autoserialize
  public name: string;

  @autoserialize
  public displayName: string;

  @autoserialize
  public buildNumber: number;

  @autoserialize
  public duration: number;

  @autoserialize
  public estimateDuration: number;

  @autoserialize
  public result: string;

  @autoserialize
  public set startedAt(value: number) {
    this._startedAt = value;
    this.dateStartedAt = new Date(value);
  }

  public get startedAt(): number {
    return this._startedAt;
  }

  @autoserialize
  public url: string;

  @autoserialize
  public folder: string;

}
