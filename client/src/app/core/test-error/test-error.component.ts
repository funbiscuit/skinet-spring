import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-test-error',
  templateUrl: './test-error.component.html',
  styleUrls: ['./test-error.component.scss']
})
export class TestErrorComponent implements OnInit {
  baseUrl = environment.apiUrl
  validationErrors: any
  Object = Object

  constructor(private http: HttpClient) {
  }

  ngOnInit(): void {
  }

  get404Error() {
    this.http.get(this.baseUrl + 'products/0').subscribe(response => {
      console.log(response)
    }, error => {
      console.log(error)
    })
  }

  get500Error() {
    this.http.get(this.baseUrl + 'buggy/server-error').subscribe(response => {
      console.log(response)
    }, error => {
      console.log(error)
    })
  }

  get400Error() {
    this.http.get(this.baseUrl + 'buggy/bad-request').subscribe(response => {
      console.log(response)
    }, error => {
      console.log(error)
    })
  }

  get400Validation() {
    this.http.post(this.baseUrl + 'buggy/save', {name: ''}).subscribe(response => {
      console.log(response)
    }, error => {
      console.log(error)
      this.validationErrors = error.errors
    })
  }

}
