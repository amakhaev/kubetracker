import {RetrieveStrategy} from "./retrieve-strategy";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {JenkinsJobsService} from "../../shared/services/jenkins-jobs.service";

/**
 * Provides the retrieve strategy for last builds
 */
export class LastBuildsRetrieveStrategy implements RetrieveStrategy {

  private static LAST_BUILDS_COUNT: number = 5;

  /**
   * Initialize new instance of ActiveBuildsRetrieveStrategy
   *
   * @param jenkinsJobService - the JenkinsJobsService instance
   */
  constructor(private jenkinsJobService: JenkinsJobsService) {
  }

  /**
   * Gets the title of strategy
   */
  public getTitle(): string {
    return "Last " + LastBuildsRetrieveStrategy.LAST_BUILDS_COUNT + " builds";
  }

  /**
   * Retrieves the jenkins build results
   */
  public retrieveBuilds(): Promise<JenkinsJobModel[]> {
    return this.jenkinsJobService.retrieveLastBuildJobs(LastBuildsRetrieveStrategy.LAST_BUILDS_COUNT);
  }

}
