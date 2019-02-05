import {Component, Input} from "@angular/core";
/**
 * Provides the widget that represents result of UI tests execution from jenkins
 */
@Component({
  selector: 'ui-test-widget',
  templateUrl: './ui-test-widget.component.html',
  styleUrls: ['./ui-test-widget.component.scss']
})
export class UiTestWidgetComponent {

  /**
   * Provides the environment for which data will be retrieved
   */
  @Input() public environment: string;

}
