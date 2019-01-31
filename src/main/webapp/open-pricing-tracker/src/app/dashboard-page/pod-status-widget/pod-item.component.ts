import {Component, Input} from "@angular/core";
import {PodModel} from "../../shared/models/pod.model";

/**
 * Provides the component that shown status of pod
 */
@Component({
  selector: 'pod-item',
  templateUrl: './pod-item.component.html',
  styleUrls: ['./pod-item.component.scss']
})
export class PodItemComponent {

  /**
   * The pod item
   */
  @Input() public item: PodModel;

  /**
   * Gets the age of pod item
   */
  public get age(): string {
    let differentSeconds: number = (Date.now() - this.item.startedAt.getTime()) / 1000;
    let days: number = Math.floor(differentSeconds / (3600*24));
    differentSeconds -= days * 3600 * 24;

    let hours: number = Math.floor(differentSeconds / 3600);
    differentSeconds -= hours * 3600;

    let minutes: number = Math.floor(differentSeconds / 60);

    return (days === 0 ? "" : days + "d ") + hours + "h " + minutes + "m";
  }
}
