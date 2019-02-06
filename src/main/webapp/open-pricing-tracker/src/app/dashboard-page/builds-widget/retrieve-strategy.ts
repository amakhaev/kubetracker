import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
/**
 * Provides the common interface for retrieve builds
 */
export interface RetrieveStrategy {

  /**
   * Retrieves the jenkins build results
   */
  retrieveBuilds(): Promise<JenkinsJobModel[]>;

}
