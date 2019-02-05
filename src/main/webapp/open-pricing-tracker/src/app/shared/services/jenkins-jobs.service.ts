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
  public retrieveUiTestJob(env: "DEV" | "QA" | string, suite: "SMOKE" | "END_TO_END" | string): Promise<JenkinsJobModel> {
    return new Promise((resolve, reject) => {
      let params = new HttpParams();
      params = params.append("environment", env);
      params = params.append("suite", suite);

      this.http.get(environment.baseApiUrl + environment.urls.jenkinsJobUiTest, {params}).subscribe(
        data => {
          let uiTestJob: JenkinsJobModel = <JenkinsJobModel>data;
          resolve(Deserialize(uiTestJob, JenkinsJobModel));
        },
        err => reject(err)
      );
    });
  };

  /**
   * Retrieves the list of active build
   */
  public retrieveActiveBuildJobs(): Promise<JenkinsJobModel[]> {
    return new Promise((resolve, reject) => {
      this.http.get(environment.baseApiUrl + environment.urls.jenkinsJobActiveBuilds).subscribe(
        data => {
          let activeBuildJobs: JenkinsJobModel[] = <JenkinsJobModel[]>data;
          resolve(activeBuildJobs.map(ab => Deserialize(ab, JenkinsJobModel)));
        },
        err => reject(err)
      );
    });
  };

  /**
   * Retrieves the list of last builds
   */
  public retrieveLastBuildJobs(count: number): Promise<JenkinsJobModel[]> {
    return new Promise((resolve, reject) => {
      let params = new HttpParams();
      params = params.append("count", count.toLocaleString());

      this.http.get(environment.baseApiUrl + environment.urls.jenkinsJobLastBuilds, {params}).subscribe(
        data => {
          let activeBuildJobs: JenkinsJobModel[] = <JenkinsJobModel[]>data;
          resolve(activeBuildJobs.map(ab => Deserialize(ab, JenkinsJobModel)));
        },
        err => reject(err)
      );
    });
  };
}
