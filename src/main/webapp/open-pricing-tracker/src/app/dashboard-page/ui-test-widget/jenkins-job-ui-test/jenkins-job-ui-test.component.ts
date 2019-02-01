import {Component, Input, OnDestroy, OnInit} from "@angular/core";
import {JenkinsJobsService} from "../../../shared/services/jenkins-jobs.service";
import {JenkinsJobModel} from "../../../shared/models/jenkins-job.model";
import {JenkinsJobResult} from "../../../shared/models/jenkins-job-result.enum";
import {DatePipe} from "@angular/common";
/**
 * Provides the dashboard component
 */
@Component({
  selector: 'jenkins-job-ui-test',
  templateUrl: './jenkins-job-ui-test.component.html',
  styleUrls: ['./jenkins-job-ui-test.component.scss']
})
export class JenkinsJobUiTestComponent implements OnInit, OnDestroy {

  private _updateInterval: any;

  /**
   * Provides the environment for which data will be retrieved
   */
  @Input() public environment: string;

  /**
   * Provides types of tests that were ran (Smoke, e2e)
   */
  @Input() public testsType: string;

  /**
   * Provides the model that described status of jenkins job
   */
  public uiTestStatus: JenkinsJobModel;

  /**
   * Provides the type of result
   */
  public jenkinsJobResult = JenkinsJobResult;

  /**
   * Gets the result of execution current UI test job
   */
  public get result(): JenkinsJobResult {
    if (!this.uiTestStatus) {
      return JenkinsJobResult.NOT_BUILT;
    } else if (!this.uiTestStatus.result) {
      return JenkinsJobResult.IN_PROGRESS;
    } else if (this.uiTestStatus.result.toLowerCase() === "not_built") {
      return JenkinsJobResult.NOT_BUILT;
    } else if (this.uiTestStatus.result.toLowerCase() === "failure") {
      return JenkinsJobResult.FAILURE;
    } else if (this.uiTestStatus.result.toLowerCase() === "success") {
      return JenkinsJobResult.SUCCESS;
    } else if (this.uiTestStatus.result.toLowerCase() === "aborted") {
      return JenkinsJobResult.ABORTED;
    } else {
      return JenkinsJobResult.NOT_BUILT;
    }
  }

  /**
   * Gets the title of component
   */
  public get title(): string {
    if (!this.uiTestStatus) {
      return "";
    }

    let testTypeDisplayName: string;
    if (this.testsType.toLowerCase() === 'smoke') {
      testTypeDisplayName = "Smoke";
    } else if (this.testsType.toLowerCase() === 'end_to_end') {
      testTypeDisplayName = "E2E";
    } else {
      testTypeDisplayName = "Unknown";
    }

    let buildNumberDisplayName: string;
    if (this.uiTestStatus.buildNumber && this.uiTestStatus.buildNumber > 0) {
      buildNumberDisplayName = "(#" + this.uiTestStatus.buildNumber + ")"
    } else {
      buildNumberDisplayName = "(No builds)"
    }

    return testTypeDisplayName + " " + buildNumberDisplayName;
  }

  /**
   * Gets the start date as string representation
   */
  public get startDate(): string {
    if (!this.uiTestStatus || !this.uiTestStatus.startedAt) {
      return "";
    }

    return this.datePipe.transform(this.uiTestStatus.startedAt, "medium");
  }

  public get estimateTime(): string {
    if (!this.uiTestStatus || !this.uiTestStatus.startedAt) {
      return "";
    }

    let differenceMilliseconds: number = Date.now() - this.uiTestStatus.startedAt;
    if (differenceMilliseconds <= 0 ||
      this.uiTestStatus.estimateDuration <= 0 ||
      this.uiTestStatus.estimateDuration <= differenceMilliseconds) {
      return 0 + "s";
    }

    let currentDuration: number = (this.uiTestStatus.estimateDuration - differenceMilliseconds) / 1000;
    let days: number = Math.floor(currentDuration / (3600*24));
    currentDuration -= days * 3600 * 24;

    let hours: number = Math.floor(currentDuration / 3600);
    currentDuration -= hours * 3600;

    let minutes: number = Math.floor(currentDuration / 60);
    return (hours === 0 ? "" : hours + "h ") + minutes + "m";
  }

  /**
   * Indicates when component is clickable
   */
  public get isClickable(): boolean {
    return this.uiTestStatus && this.uiTestStatus.url !== "UNKNOWN";
  }

  /**
   * Initialize new instance of JenkinsJobUiTestComponent
   *
   * @param jenkinsJobService - the JenkinsJobsService instance
   */
  constructor(private jenkinsJobService: JenkinsJobsService, private datePipe: DatePipe) {
  }

  public ngOnInit(): void {
    this.refreshData();
    this._updateInterval = setInterval(() => { this.refreshData(); }, 30000);
  }

  public ngOnDestroy(): void {
    clearInterval(this._updateInterval);
  }

  /**
   * Handles the click by component
   */
  public onClick(): void {
    if (this.uiTestStatus && this.uiTestStatus.url !== "UNKNOWN") {
      window.open(this.uiTestStatus.url, '_blank');
    }
  }

  private refreshData() {
    this.jenkinsJobService.retrieveUiTestJob(this.environment.toUpperCase(), this.testsType.toUpperCase()).then(
      result => {
        this.uiTestStatus = result;
      },
      err => console.error(err)
    );
  }
}
