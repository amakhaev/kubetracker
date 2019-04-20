import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public randomImageApiUrl: String;
  public timeToShowImage: Boolean;

  constructor() {
    this.timeToShowImage = false;
  }

  ngOnInit(): void {
    setInterval(() => {
      this.timeToShowImage = true;
      this.randomImageApiUrl = '../assets/images/' + (Math.floor(Math.random() * 10) + 1) + '.jpg';
      setTimeout(() => this.timeToShowImage = false, 180000);
    }, 18180000);
  }
}
