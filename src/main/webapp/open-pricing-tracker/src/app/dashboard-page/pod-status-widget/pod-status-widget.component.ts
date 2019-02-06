import {Component, HostListener, Input, OnDestroy, OnInit} from "@angular/core";
import {PodListService} from "../../shared/services/pod-list.service";
import {PodModel} from "../../shared/models/pod.model";
import {isNullOrUndefined} from "util";
import {ContainerState} from "../../shared/components/container-size/container-state.enum";

/**
 * Provides the widget that shown status of podList on specific environment
 */
@Component({
  selector: 'pod-status-widget',
  templateUrl: './pod-status-widget.component.html',
  styleUrls: ['./pod-status-widget.component.scss']
})
export class PodStatusWidgetComponent implements OnInit, OnDestroy {

  private _updateInterval: any;
  private _windowHeight: number;

  /**
   * The namespace for looking on watched component
   */
  @Input() public namespace: string;

  @HostListener('window:resize', ['$event'])
  public onResize(event) {
    this._windowHeight = window.innerHeight;
  }

  /**
   * The list of pods that available for current namespace
   */
  public pods: PodModel[];

  /**
   * Indicates when component tried to refresh data
   */
  public isLoading: boolean;

  /**
   * Provides the current state of container
   */
  public currentContainerState: ContainerState = ContainerState.MAXIMUM;

  /**
   * Provides the type of container state
   */
  public containerState = ContainerState;

  /**
   * Indicates when all pods has a ready state
   */
  public get isAllPodsReady(): boolean {
    return this.pods && isNullOrUndefined(this.pods.find(p => !p.ready));
  }

  /**
   * Gets the height of component.
   */
  public get componentHeight(): number {
    if (!this._windowHeight) {
      return 540;
    }

    let calculatedHeight: number = this._windowHeight / 100 * 40;
    return calculatedHeight >= 540 ? calculatedHeight : 540;
  }

  /**
   * Gets the message of minimal widget state
   */
  public get messageForMinimalState(): string {
    if (isNullOrUndefined(this.pods) || this.pods.length === 0) {
      return "No records found"
    }

    if (this.isAllPodsReady) {
      return "Running " + this.pods.length;
    }

    let count: number = this.pods.filter(pod => !pod.ready).length;
    return "Not ready " + count + " of " + this.pods.length;
  }

  /**
   * Initialize new instance of PodStatusWidgetComponent
   *
   * @param podListService - the PodListService instance
   */
  constructor(private podListService: PodListService) {
  }

  public ngOnInit(): void {
    this._windowHeight = window.innerHeight;
    this.refreshData();
    this._updateInterval = setInterval(() => { this.refreshData(); }, 15000);
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
    this.isLoading = true;
    this.podListService.retrievePods(this.namespace, true).then(
      pods => {
        this.pods = pods;
        this.isLoading = false;
      },
      err => {
        console.error(err);
        this.pods = [];
        this.isLoading = false;
      }
    );
  }
}
