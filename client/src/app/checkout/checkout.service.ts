import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {DeliveryMethod} from "../shared/models/deliveryMethod";
import {map} from "rxjs/operators";
import {Order, OrderToCreate} from "../shared/models/order";
import {ReplaySubject} from "rxjs";

interface StripeConfig {
  publishableKey: string
}

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  baseUrl = environment.apiUrl
  private stripeConfigSource = new ReplaySubject<StripeConfig>(1)
  stripeConfig$ = this.stripeConfigSource.asObservable()

  constructor(private http: HttpClient) {
    this.http.get<StripeConfig>('assets/config/stripe.json').subscribe(config => {
      this.stripeConfigSource.next(config)
    }, error => {
      this.stripeConfigSource.next({publishableKey: "pk_test_YOUR_KEY"})
    })
  }

  getDeliveryMethods() {
    return this.http.get<DeliveryMethod[]>(this.baseUrl + 'orders/delivery-methods').pipe(
      map(dm => {
        return dm.sort((a, b) => b.price - a.price);
      })
    )
  }

  createOrder(order: OrderToCreate) {
    return this.http.post<Order>(this.baseUrl + 'orders', order)
  }
}
