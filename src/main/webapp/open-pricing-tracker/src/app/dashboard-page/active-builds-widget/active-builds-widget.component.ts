import {Component, Input, OnDestroy, OnInit} from "@angular/core";
import {JenkinsJobsService} from "../../shared/services/jenkins-jobs.service";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
/**
 * Provides the active builds list component
 */
@Component({
  selector: 'active-builds',
  templateUrl: './active-builds-widget.component.html',
  styleUrls: ['./active-builds-widget.component.scss']
})
export class ActiveBuildsWidgetComponent implements OnInit, OnDestroy {

  private _refreshDataFunction: Function;

  /**
   * Provides hte active jenkins builds
   */
  public activeBuilds: JenkinsJobModel[];

  /**
   * Initialize new instance of ActiveBuildsWidgetComponent
   *
   * @param jenkinsJobService - the JenkinsJobsService instance
   */
  constructor(private jenkinsJobService: JenkinsJobsService) {
  }

  public ngOnInit(): void {
    this._refreshDataFunction = this.refreshData;
    this.refreshData();
  }

  public ngOnDestroy(): void {
    this._refreshDataFunction = null;
  }

  private refreshData(): void {
    this.jenkinsJobService.retrieveActiveBuildJobs().then(
      activeBuilds => {
        this.activeBuilds = activeBuilds;
        if (this._refreshDataFunction) {
          this._refreshDataFunction();
        }
      },
      err => {
        this.activeBuilds = [];
        console.error(err);
      }
    );
  }
}
