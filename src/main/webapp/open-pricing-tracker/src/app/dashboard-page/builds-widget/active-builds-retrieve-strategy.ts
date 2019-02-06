import {RetrieveStrategy} from "./retrieve-strategy";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {JenkinsJobsService} from "../../shared/services/jenkins-jobs.service";

/**
 * Provides the retrieve strategy for active builds
 */
export class ActiveBuildsRetrieveStrategy implements RetrieveStrategy {

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
    return this.jenkinsJobService.retrieveActiveBuildJobs();
  }


}
