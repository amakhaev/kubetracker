import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  public randomImageApiUrl: string;
  public timeToShowImage: boolean;

  constructor() {
    this.timeToShowImage = false;
  }

  ngOnInit(): void {
    setInterval(() => {
      this.timeToShowImage = true;
      this.randomImageApiUrl = '../assets/images/' + (Math.floor(Math.random() * 11) + 1) + '.jpg';
      setTimeout(() => this.timeToShowImage = false, 3000);
    }, 18003000);
  }
}
