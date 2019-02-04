import {Pipe, PipeTransform} from "@angular/core";
import {isNullOrUndefined} from "util";

/**
 * Provides the pipe that converts number value to age like 5d 10h 12m
 */
@Pipe({
  name: 'agePipe'
})
export class AgePipe implements PipeTransform {

  transform(milliseconds: number): any {
    if (isNullOrUndefined(milliseconds)) {
      return "N/A";
    }

    if (milliseconds <= 0) {
      return 0 + "s";
    }

    let seconds = milliseconds / 1000;

    let days: number = Math.floor(seconds / (3600*24));
    seconds -= days * 3600 * 24;

    let hours: number = Math.floor(seconds / 3600);
    seconds -= hours * 3600;

    let minutes: number = Math.floor(seconds / 60);

    let result: string = "";
    if (days > 0) {
      result += days + "d " + hours + "h " + minutes + "m";
    } else if (hours > 0) {
      result += hours + "h " + minutes + "m";
    } else {
      result += minutes + "m";
    }
    return result;
  }

}
