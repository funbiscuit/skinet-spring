import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Order} from "../shared/models/order";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  baseUrl = environment.apiUrl

  constructor(private http: HttpClient) {
  }

  getOrders() {
    return this.http.get<Order[]>(this.baseUrl + 'orders')
  }

  getOrder(id: number) {
    return this.http.get<Order>(this.baseUrl + 'orders/' + id)
  }
}
