import {Injectable} from "@angular/core";
import {PodModel} from "../models/pod.model";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {isNullOrUndefined} from "util";
import {Deserialize} from "cerialize";

/**
 * Provides the service that makes request to server for pod list
 */
@Injectable()
export class PodListService {

  /**
   * Initialize new instance of PodListService
   *
   * @param http - the http client instance
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Retrieves the settings model from server
   */
  public retrievePods(namespace: string, isNeedApplyFilters): Promise<PodModel[]> {
    return new Promise((resolve, reject) => {
      let params = new HttpParams();
      params = params.append("namespace", namespace);
      params = params.append("applyFilters", isNeedApplyFilters);

      this.http.get(environment.baseApiUrl + environment.urls.pods, {params}).subscribe(
        data => {
          let pods: PodModel[] = <PodModel[]>data;
          resolve(isNullOrUndefined(pods) ? [] : pods.map(p => Deserialize(p, PodModel)));
        },
        err => reject(err)
      );
    });
  };

}
