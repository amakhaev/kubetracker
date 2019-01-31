import {autoserialize} from "cerialize";
/**
 * Provides the model that described filter
 */
export class FilterModel {

  @autoserialize
  public id: number;

  @autoserialize
  public filterValue: string;

}
