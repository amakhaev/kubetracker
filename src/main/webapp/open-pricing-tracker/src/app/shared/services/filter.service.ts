import {FilterModel} from "../models/filter.model";
import {environment} from "../../../environments/environment";
import {Deserialize} from "cerialize";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Injectable} from "@angular/core";
/**
 * Provides the service that worked with filters
 */
@Injectable()
export class FilterService {

  /**
   * Initialize new instance of FilterService
   *
   * @param http - the http client instance
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Retrieves the filters model from server
   */
  public retrieveFilters(): Promise<FilterModel[]> {
    return new Promise((resolve, reject) => {
      this.http.get(environment.baseApiUrl + environment.urls.filters).subscribe(
        data => {
          let filters = <FilterModel[]>data;
          filters = filters.map(f => Deserialize(f, FilterModel));
          resolve(filters);
        },
        err => reject(err)
      );
    });
  };

  /**
   * Adds new filter for pods
   *
   * @param filterValue - the value of filter
   */
  public addFilter(filterValue: string): Promise<FilterModel[]> {
    return new Promise((resolve, reject) => {
      let filter: FilterModel = new FilterModel();
      filter.filterValue = filterValue;
      this.http.post(environment.baseApiUrl + environment.urls.filters, filter).subscribe(
        data => {
          let filters = <FilterModel[]>data;
          filters = filters.map(f => Deserialize(f, FilterModel));
          resolve(filters);
        },
        err => reject(err)
      );
    });
  };

  /**
   * Deletes filter for pods
   *
   * @param id - the id of filter to delete
   */
  public deleteFilter(id: number): Promise<FilterModel[]> {
    return new Promise((resolve, reject) => {
      let params = new HttpParams();
      params = params.append("id", id.toString());

      this.http.delete(environment.baseApiUrl + environment.urls.filters, {params}).subscribe(
        data => {
          let filters = <FilterModel[]>data;
          filters = filters.map(f => Deserialize(f, FilterModel));
          resolve(filters);
        },
        err => reject(err)
      );
    });
  };
}
