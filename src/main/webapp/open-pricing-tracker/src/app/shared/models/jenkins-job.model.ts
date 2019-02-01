import {autoserialize, autoserializeAs} from "cerialize";
/**
 * Provides the model that described jenkins job
 */
export class JenkinsJobModel {

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
  public startedAt: number;

  @autoserialize
  public url: string;

}
