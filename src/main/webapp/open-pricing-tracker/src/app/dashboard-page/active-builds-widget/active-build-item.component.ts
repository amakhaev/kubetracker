import {Component, Input, OnInit} from "@angular/core";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";

/**
 * Provides the active build item component
 */
@Component({
  selector: 'active-build-item',
  templateUrl: './active-build-item.component.html',
  styleUrls: ['./active-build-item.component.scss']
})
export class ActiveBuildItemComponent {

  /**
   * Provides the build job from jenkins
   */
  @Input() buildItem: JenkinsJobModel;

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
