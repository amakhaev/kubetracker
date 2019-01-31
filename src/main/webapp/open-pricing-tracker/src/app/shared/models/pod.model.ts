import {autoserialize, autoserializeAs} from "cerialize";
/**
 * Provides the model that described the pod
 */
export class PodModel {

  @autoserialize
  public name: string;

  @autoserialize
  public namespace: string;

  @autoserialize
  public status: string;

  @autoserialize
  public restartCount: number;

  @autoserializeAs(Date)
  public startedAt: Date;

  @autoserialize
  public ready: boolean;
}
