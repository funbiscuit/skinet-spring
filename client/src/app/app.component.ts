import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Product} from "./models/products";
import {Pagination} from "./models/pagination";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'SkiNet';
  products: Product[] = [];

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
    this.http.get('https://localhost:5001/api/products?size=6')
      .subscribe((response: any) => {
        const pagination = response as Pagination
        console.log(response)
        this.products = pagination.content;
      }, error => {
        console.log(error)
      })
  }

}
