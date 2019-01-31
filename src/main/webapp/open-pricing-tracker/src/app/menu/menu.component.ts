import { Component } from '@angular/core';
import {Router} from "@angular/router";
import { environment } from '../../environments/environment';

@Component({
  selector: 'tracker-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  /**
   * Initialize new instance of MenuComponent
   *
   * @param router - the router instance
   */
  constructor(private router: Router) {
  }

  /**
   * Handles the click on 'dashboard' icon.
   */
  public onDashboardClick() {
    this.router.navigate([environment.navigation.dashboard]);
  }

  /**
   * Handles the click on 'settings' icon.
   */
  public onSettingsClick() {
    this.router.navigate([environment.navigation.settings]);
  }

  /**
   * Handles the click on 'info' icon.
   */
  public onInfoClick() {
    window.open(environment.navigation.swagger, '_blank');
  }

  /**
   * Handles the click on 'info' icon.
   */
  public onFilterClick() {
    this.router.navigate([environment.navigation.filters]);
  }

}
