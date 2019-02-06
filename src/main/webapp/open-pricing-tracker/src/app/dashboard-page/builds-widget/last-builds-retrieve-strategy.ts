import {RetrieveStrategy} from "./retrieve-strategy";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {JenkinsJobsService} from "../../shared/services/jenkins-jobs.service";
import {JenkinsJobResult} from "../../shared/models/jenkins-job-result.enum";

/**
 * Provides the retrieve strategy for last builds
 */
export class LastBuildsRetrieveStrategy implements RetrieveStrategy {

  public static LAST_BUILDS_COUNT: number = 5;

  /**
   * Initialize new instance of ActiveBuildsRetrieveStrategy
   *
   * @param jenkinsJobService - the JenkinsJobsService instance
   */
  constructor(private jenkinsJobService: JenkinsJobsService) {
  }

  /**
   * Retrieves the jenkins build results
   */
  public retrieveBuilds(): Promise<JenkinsJobModel[]> {
    return this.jenkinsJobService.retrieveLastBuildJobs(LastBuildsRetrieveStrategy.LAST_BUILDS_COUNT);
  }
}
