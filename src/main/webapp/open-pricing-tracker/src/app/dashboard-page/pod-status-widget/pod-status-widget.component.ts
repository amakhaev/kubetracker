import {Component, Input, OnInit} from "@angular/core";
import {PodListService} from "../../shared/services/pod-list.service";
import {PodModel} from "../../shared/models/pod.model";
import {isNullOrUndefined} from "util";

/**
 * Provides the widget that shown status of podList on specific environment
 */
@Component({
  selector: 'pod-status-widget',
  templateUrl: './pod-status-widget.component.html',
  styleUrls: ['./pod-status-widget.component.scss']
})
export class PodStatusWidgetComponent implements OnInit {

  /**
   * The namespace for looking on watched component
   */
  @Input() public namespace: string;

  /**
   * The list of pods that available for current namespace
   */
  public pods: PodModel[];

  /**
   * Provides the last data refresh time
   */
  public lastRefreshSeconds: number;

  public isLoading: boolean;

  /**
   * Indicates when all pods has a ready state
   */
  public get isAllPodsReady(): boolean {
    return this.pods && isNullOrUndefined(this.pods.find(p => !p.ready));
  }

  /**
   * Initialize new instance of PodStatusWidgetComponent
   *
   * @param podListService - the PodListService instance
   */
  constructor(private podListService: PodListService) {
  }

  public ngOnInit(): void {
    this.lastRefreshSeconds = 0;
    this.refreshData();
    setInterval(() => { this.refreshData(); }, 15000);
    setInterval(() => { this.lastRefreshSeconds++; }, 1000);
  }

  private refreshData(): void {
    this.isLoading = true;
    this.podListService.retrievePods(this.namespace, true).then(
      pods => {
        this.pods = pods;
        this.lastRefreshSeconds = 0;
        this.isLoading = false;
      },
      err => {
        console.error(err);
        this.pods = [];
        this.lastRefreshSeconds = 0;
        this.isLoading = false;
      }
    );
  }
}
