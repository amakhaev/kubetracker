import {Component, Input, OnDestroy, OnInit} from "@angular/core";
import {JenkinsJobsService} from "../../shared/services/jenkins-jobs.service";
import {JenkinsJobModel} from "../../shared/models/jenkins-job.model";
import {RetrieveStrategy} from "./retrieve-strategy";
import {isNullOrUndefined} from "util";
import {ContainerState} from "../../shared/components/container-size/container-state.enum";
import {BuildsWidgetData} from "./builds-widget-data";
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
   * Provides the data of widget
   */
  @Input() widgetData: BuildsWidgetData;

  /**
   * Provides the
   */
  @Input() customHeight: number;

  /**
   * Provides hte active jenkins builds
   */
  public activeBuilds: JenkinsJobModel[];

  /**
   * Provides the current state of container
   */
  public currentContainerState: ContainerState = ContainerState.MAXIMUM;

  /**
   * Provides the type of container state
   */
  public containerState = ContainerState;

  /**
   * Gets the style class
   */
  public get styleClass(): string {
    return this.widgetData.getStyleClass(this.activeBuilds);
  }

  /**
   * Gets the message for container in minimal state
   */
  public get minimalContainerMessage(): string {
    return this.widgetData.getMessage(this.activeBuilds);
  }

  public ngOnInit(): void {
    this._updateInterval = setInterval(() => { this.refreshData(); }, 15000);
    this.refreshData();
  }

  public ngOnDestroy(): void {
    clearInterval(this._updateInterval);
  }

  /**
   * Handles the changing of container state
   *
   * @param state - the new state of component
   */
  public onStateChanged(state: ContainerState): void {
    this.currentContainerState = state;
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
