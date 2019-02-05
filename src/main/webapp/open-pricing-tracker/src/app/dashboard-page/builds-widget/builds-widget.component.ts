import {Component, Input, OnDestroy, OnInit} from "@angular/core";
import {JenkinsJobsService} from "../../shared/services/jenkins-jobs.service";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {RetrieveStrategy} from "./retrieve-strategy";
import {isNullOrUndefined} from "util";
/**
 * Provides the active builds list component
 */
@Component({
  selector: 'active-builds',
  templateUrl: './builds-widget.component.html',
  styleUrls: ['./builds-widget.component.scss']
})
export class BuildsWidgetComponent implements OnInit, OnDestroy {

  private _updateInterval: any;

  /**
   * Provides the retrieve strategy of builds
   */
  @Input() retrieveStrategy: RetrieveStrategy;

  /**
   * Provides the
   */
  @Input() customHeight: number;

  /**
   * Provides hte active jenkins builds
   */
  public activeBuilds: JenkinsJobModel[];

  public ngOnInit(): void {
    this._updateInterval = setInterval(() => { this.refreshData(); }, 15000);
    this.refreshData();
  }

  public ngOnDestroy(): void {
    clearInterval(this._updateInterval);
  }

  private refreshData(): void {
    this.retrieveStrategy.retrieveBuilds().then(
      activeBuilds => {
        this.activeBuilds = activeBuilds;
      },
      err => {
        this.activeBuilds = [];
        console.error(err);
      }
    );
  }
}
