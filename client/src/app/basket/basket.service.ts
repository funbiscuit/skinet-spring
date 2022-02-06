import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";
import {
  Basket,
  BasketItem,
  BasketTotals,
  IBasket
} from "../shared/models/basket";
import {map} from "rxjs/operators";
import {Product} from "../shared/models/products";
import {DeliveryMethod} from "../shared/models/deliveryMethod";

@Injectable({
  providedIn: 'root'
})
export class BasketService {
  baseUrl = environment.apiUrl;
  private basketSource = new BehaviorSubject<IBasket | null>(null)
  basket$ = this.basketSource.asObservable()
  private basketTotalSource = new BehaviorSubject<BasketTotals | null>(null)
  basketTotal$ = this.basketTotalSource.asObservable()

  shipping = 0

  constructor(private http: HttpClient) {
  }

  createPaymentIntent() {
    return this.http.post<IBasket>(this.baseUrl + 'payments/' + this.getCurrentBasketValue().id, {})
      .pipe(
        map(basket => {
          this.basketSource.next(basket)
        })
      )
  }

  setShippingPrice(deliveryMethod: DeliveryMethod) {
    this.shipping = deliveryMethod.price

    const basket = this.getCurrentBasketValue()
    basket.deliveryMethodId = deliveryMethod.id
    basket.shippingPrice = this.shipping

    this.calculateTotals()
    this.setBasket(basket)
  }

  getBasket(id: string) {
    return this.http.get<IBasket>(this.baseUrl + 'basket?id=' + id)
      .pipe(
        map(basket => {
          this.basketSource.next(basket)
          this.shipping = basket.shippingPrice ?? 0
          this.calculateTotals()
        })
      )
  }

  setBasket(basket: IBasket) {
    return this.http.post<IBasket>(this.baseUrl + 'basket', basket)
      .subscribe(response => {
        this.basketSource.next(response)
        this.calculateTotals()
      }, error => {
        console.log(error)
      })
  }

  getCurrentBasketValue() {
    return this.basketSource.value ?? BasketService.createBasket()
  }

  addItemToBasket(item: Product, quantity = 1) {
    const itemToAdd = BasketService.mapProductToBasketItem(item, quantity)
    const basket = this.getCurrentBasketValue()
    basket.items = this.addOrUpdateItem(basket.items, itemToAdd, quantity)
    this.setBasket(basket)
  }

  incrementItemQuantity(item: BasketItem) {
    const basket = this.getCurrentBasketValue()
    const itemIndex = basket.items.findIndex(x => x.id === item.id)
    basket.items[itemIndex].quantity++;
    this.setBasket(basket)
  }

  decrementItemQuantity(item: BasketItem) {
    const basket = this.getCurrentBasketValue()
    const itemIndex = basket.items.findIndex(x => x.id === item.id)
    basket.items[itemIndex].quantity--;
    if (basket.items[itemIndex].quantity <= 0) {
      this.removeItemFromBasket(item);
    }
    this.setBasket(basket)
  }

  removeItemFromBasket(item: BasketItem) {
    const basket = this.getCurrentBasketValue()
    // if (basket.items.some(x => x.id === item.id)) {
    basket.items = basket.items.filter(i => i.id !== item.id)
    if (basket.items.length > 0) {
      this.setBasket(basket)
    } else {
      this.deleteBasket(basket)
    }
    // }
  }

  private static mapProductToBasketItem(item: Product, quantity: number): BasketItem {
    return {
      id: item.id,
      brand: item.brand,
      productName: item.name,
      type: item.type,
      pictureUrl: item.pictureUrl,
      price: item.price,
      quantity
    }
  }

  private static createBasket(): IBasket {
    const basket = new Basket()
    localStorage.setItem('basket_id', basket.id)
    return basket
  }

  private addOrUpdateItem(items: BasketItem[], itemToAdd: BasketItem, quantity: number): BasketItem[] {
    const index = items.findIndex(i => i.id === itemToAdd.id)
    if (index === -1) {
      itemToAdd.quantity = quantity
      items.push(itemToAdd)
    } else {
      items[index].quantity += quantity
    }
    return items
  }

  private calculateTotals() {
    const basket = this.getCurrentBasketValue()
    const shipping = this.shipping
    const subtotal = basket.items.reduce((a, b) =>
      (b.price * b.quantity) + a, 0)
    this.basketTotalSource.next({
      shipping,
      subtotal,
      total: shipping + subtotal
    })
  }

  deleteLocalBasket() {
    this.basketSource.next(null)
    this.basketTotalSource.next(null)
    localStorage.removeItem('basket_id')
  }

  deleteBasket(basket: IBasket) {
    return this.http.delete(this.baseUrl + 'basket?id=' + basket.id)
      .subscribe(() => {
        this.deleteLocalBasket()
      }, error => {
        console.log(error)
      })
  }
}
