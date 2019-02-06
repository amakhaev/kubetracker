import {Component, EventEmitter, Input, Output} from "@angular/core";
import {ContainerState} from "./container-state.enum";
import {isNullOrUndefined} from "util";

/**
 * Provides the container size component
 */
@Component({
  selector: 'container-size',
  templateUrl: './container-size.component.html',
  styleUrls: ['./container-size.component.scss']
})
export class ContainerSizeComponent {

  /**
   * The current state of container
   */
  @Input() public state: ContainerState;

  /**
   * Provides the event emitter of state
   */
  @Output() public onStateChanged = new EventEmitter();

  /**
   * Provides the type of container state
   */
  public containerState = ContainerState;

  /**
   * Indicates when maximum state is available
   */
  public get isMaximumStateAvailable(): boolean {
    return !isNullOrUndefined(this.state) && this.state === ContainerState.MAXIMUM;
  }

  /**
   * Indicates when minimum state is available
   */
  public get isMinimalStateAvailable(): boolean {
    return !isNullOrUndefined(this.state) && this.state === ContainerState.MINIMAL;
  }

  /**
   * Handles the click by icon
   *
   * @param state - the state that provides by icon
   */
  public onIconClick(state: ContainerState): void {
    this.onStateChanged.emit(state);
  }
}
