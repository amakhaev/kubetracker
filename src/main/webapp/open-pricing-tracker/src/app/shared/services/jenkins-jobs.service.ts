import {JenkinsJobModel} from "../models/jenkins-job.model";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Deserialize} from "cerialize";
import {environment} from "../../../environments/environment";
import {Injectable} from "@angular/core";
/**
 * Provides the service that worked with jenkins jobs
 */
@Injectable()
export class JenkinsJobsService {

  /**
   * Initialize new instance of FilterService
   *
   * @param http - the http client instance
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Retrieves the result of test job
   */
  public retrieveUiTestJob(env: "DEV" | "QA" | string, type: "SMOKE" | "END_TO_END" | string): Promise<JenkinsJobModel> {
    return new Promise((resolve, reject) => {
      let params = new HttpParams();
      params = params.append("environment", env);
      params = params.append("jobType", type);

      this.http.get(environment.baseApiUrl + environment.urls.jenkinsJobUiTest, {params}).subscribe(
        data => {
          let uiTestJob: JenkinsJobModel = <JenkinsJobModel>data;
          resolve(Deserialize(uiTestJob, JenkinsJobModel));
        },
        err => reject(err)
      );
    });
  };

}
