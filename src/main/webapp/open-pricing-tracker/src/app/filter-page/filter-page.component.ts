import {Component, OnInit} from "@angular/core";
import {FilterService} from "../shared/services/filter.service";
import {FilterModel} from "../shared/models/filter.model";
import {isNullOrUndefined} from "util";

/**
 * Provides the filter component related to pods
 */
@Component({
  selector: 'filters',
  templateUrl: './filter-page.component.html',
  styleUrls: ['./filter-page.component.scss']
})
export class FilterPageComponent implements OnInit {

  /**
   * Provides the list of filters that already available
   */
  public availableFilters: FilterModel[];

  /**
   * Provides the new value of filter
   */
  public newFilterValue: string;

  /**
   * Initialize new instance of FilterPageComponent
   */
  constructor(private filterService: FilterService) {
  }

  public ngOnInit(): void {
    this.filterService.retrieveFilters().then(
      filters => this.availableFilters = filters,
      err => console.error(err)
    );
  }

  /**
   * Handles the deleting of filter
   *
   * @param filter - the filter to be deleted
   */
  public onDelete(filter: FilterModel): void {
    if (!this.availableFilters || this.availableFilters.length === 0) {
      return;
    }

    this.filterService.deleteFilter(filter.id).then(
      filters => this.availableFilters = filters,
      err => console.error(err)
    );
  }

  /**
   * Handles adding of new filter
   */
  public onAdd(): void {
    if (!this.newFilterValue || this.newFilterValue === "") {
      return;
    }

    let existing: FilterModel[] = this.availableFilters.filter(f => f.filterValue.toLowerCase() === this.newFilterValue .toLowerCase());
    if (existing.length > 0) {
      return;
    }

    this.filterService.addFilter(this.newFilterValue).then(
      filters => {
        this.availableFilters = filters;
        this.newFilterValue = "";
      },
      err => console.error(err)
    );
  }

}
