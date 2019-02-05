import {Component, Input, OnInit} from "@angular/core";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {JenkinsJobResult} from "../../shared/models/jenkins-job-result.enum";
import {isNullOrUndefined} from "util";

/**
 * Provides the active build item component
 */
@Component({
  selector: 'active-build-item',
  templateUrl: './build-item.component.html',
  styleUrls: ['./build-item.component.scss']
})
export class BuildItemComponent {

  /**
   * Provides the build job from jenkins
   */
  @Input() buildItem: JenkinsJobModel;

  /**
   * Provides the type of result
   */
  public jenkinsJobResult = JenkinsJobResult;

  /**
   * Gets the result of execution current job
   */
  public get result(): JenkinsJobResult {
    if (!this.buildItem) {
      return JenkinsJobResult.NOT_BUILT;
    } else if (!this.buildItem.result) {
      return JenkinsJobResult.IN_PROGRESS;
    } else if (this.buildItem.result.toLowerCase() === "not_built") {
      return JenkinsJobResult.NOT_BUILT;
    } else if (this.buildItem.result.toLowerCase() === "failure") {
      return JenkinsJobResult.FAILURE;
    } else if (this.buildItem.result.toLowerCase() === "success") {
      return JenkinsJobResult.SUCCESS;
    } else if (this.buildItem.result.toLowerCase() === "aborted") {
      return JenkinsJobResult.ABORTED;
    } else {
      return JenkinsJobResult.NOT_BUILT;
    }
  }

  /**
   * Indicates when estimate time availabel for showing
   */
  public get isEstimateTimeAvailable(): boolean {
    return isNullOrUndefined(this.buildItem.result);
  }

  /**
   * Gets the estimate time of build
   */
  public get estimateTime(): number {
    let differenceMilliseconds: number = this.buildItem.dateNow - this.buildItem.startedAt;
    if (differenceMilliseconds <= 0 ||
      this.buildItem.estimateDuration <= 0 ||
      this.buildItem.estimateDuration <= differenceMilliseconds) {
      return 0;
    }

    return this.buildItem.estimateDuration - differenceMilliseconds;
  }

  /**
   * Handles the click by build item
   */
  public onClick() {
    if (this.buildItem && this.buildItem.url !== "UNKNOWN") {
      window.open(this.buildItem.url, '_blank');
    }
  }
}
