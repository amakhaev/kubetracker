import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {LastBuildsRetrieveStrategy} from "./last-builds-retrieve-strategy";
/**
 * Provides the data for builds widget
 */
export class BuildsWidgetData {

  /**
   * Initialize new instance of BuildsWidgetData
   */
  constructor(private widgetType: "active_builds" | "last_builds") {
  }

  /**
   * Gets the message related to specific strategy
   *
   * @param jobs - the jobs that available
   */
  public getMessage(jobs: JenkinsJobModel[]): string {
    return this.widgetType === "active_builds" ?
      this.getMessageForActiveBuildsWidget(jobs) : this.getMessageForLastBuildsWidget(jobs);
  }

  /**
   * Gets the style class
   */
  public getStyleClass(jobs: JenkinsJobModel[]): string {
    return this.widgetType === "active_builds" ?
      this.getStyleClassForActiveBuildsWidget(jobs) : this.getStyleClassForLastBuildsWidget(jobs);
  }

  /**
   * Gets the title of strategy
   */
  public getTitle(): string {
    return this.widgetType === "active_builds" ?
      this.getTitleForActiveBuildsWidget() : this.getTitleForLastBuildsWidget();
  }

  /**
   * Gets the message related to specific strategy
   *
   * @param jobs - the jobs that available
   */
  private getMessageForActiveBuildsWidget(jobs: JenkinsJobModel[]) {
    return jobs && jobs.length > 0 ? "In progress " + jobs.length : "No builds found";
  }

  /**
   * Gets the style class
   */
  private getStyleClassForActiveBuildsWidget(jobs: JenkinsJobModel[]): string {
    return jobs && jobs.length > 0 ? "in-progress" : "neutral";
  }

  /**
   * Gets the title of strategy
   */
  private getTitleForActiveBuildsWidget(): string {
    return "Active builds";
  }

  /**
   * Gets the message related to specific strategy
   *
   * @param jobs - the jobs that available
   */
  private getMessageForLastBuildsWidget(jobs: JenkinsJobModel[]) {
    if (!jobs || jobs.length === 0) {
      return "No builds found";
    }

    let failedCount: number = jobs.filter(job => job.result.toLowerCase() === "failure").length;
    return failedCount === 0 ?
      jobs.length + " successful" :
      jobs.length - failedCount + " successful, " + failedCount + " failed";
  }

  /**
   * Gets the style class
   */
  private getStyleClassForLastBuildsWidget(jobs: JenkinsJobModel[]): string {
    if (!jobs || jobs.length === 0) {
      return "success";
    }

    let failedCount: number = jobs.filter(job => job.result.toLowerCase() === "failure").length;
    return failedCount > 0 ? "failed" : "successful";
  }

  /**
   * Gets the title of strategy
   */
  private getTitleForLastBuildsWidget(): string {
    return "Last " + LastBuildsRetrieveStrategy.LAST_BUILDS_COUNT + " builds";
  }
}
