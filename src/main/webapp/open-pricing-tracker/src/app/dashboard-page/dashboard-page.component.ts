import {Component} from "@angular/core";
import {RetrieveStrategy} from "./builds-widget/retrieve-strategy";
import {ActiveBuildsRetrieveStrategy} from "./builds-widget/active-builds-retrieve-strategy";
import {JenkinsJobsService} from "../shared/services/jenkins-jobs.service";
import {LastBuildsRetrieveStrategy} from "./builds-widget/last-builds-retrieve-strategy";

/**
 * Provides the dashboard component
 */
@Component({
  selector: 'dashboard',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardComponent {

  /**
   * The retrieve strategy for active builds
   */
  public activeBuildsRetrieveStrategy: RetrieveStrategy;

  /**
   * The retrieve strategy for active builds
   */
  public lastBuildsRetrieveStrategy: RetrieveStrategy;

  /**
   * Initialize new instance of DashboardComponent
   *
   * @param jenkinsJobService - the JenkinsJobsService instance
   */
  constructor(private jenkinsJobService: JenkinsJobsService) {
    this.activeBuildsRetrieveStrategy = new ActiveBuildsRetrieveStrategy(jenkinsJobService);
    this.lastBuildsRetrieveStrategy = new LastBuildsRetrieveStrategy(jenkinsJobService);
  }
}
