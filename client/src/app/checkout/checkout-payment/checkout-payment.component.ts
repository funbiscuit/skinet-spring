import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormGroup} from "@angular/forms";
import {BasketService} from "../../basket/basket.service";
import {CheckoutService} from "../checkout.service";
import {ToastrService} from "ngx-toastr";
import {IBasket} from "../../shared/models/basket";
import {NavigationExtras, Router} from "@angular/router";

@Component({
  selector: 'app-checkout-payment',
  templateUrl: './checkout-payment.component.html',
  styleUrls: ['./checkout-payment.component.scss']
})
export class CheckoutPaymentComponent implements OnInit {

  @Input() checkoutForm!: FormGroup

  deliveryForm!: AbstractControl

  constructor(private basketService: BasketService,
              private checkoutService: CheckoutService,
              private toastr: ToastrService,
              private router: Router) {
  }

  ngOnInit(): void {
    const f = this.checkoutForm.get('deliveryForm')
    if (f) {
      this.deliveryForm = f
    }
  }

  submitOrder() {
    const basket = this.basketService.getCurrentBasketValue()
    const order = this.getOrderToCreate(basket)
    this.checkoutService.createOrder(order).subscribe(order => {
      this.toastr.success('Order created successfully')
      this.basketService.deleteLocalBasket()
      const navigationExtras: NavigationExtras = {state: order}
      this.router.navigate(['checkout/success'], navigationExtras)
    }, error => {
      this.toastr.error(error.message)
      console.log(error)
    })
  }

  private getOrderToCreate(basket: IBasket) {
    return {
      basketId: basket.id,
      deliveryMethodId: this.deliveryForm.get('deliveryMethod')?.value,
      shippingAddress: this.checkoutForm.get('addressForm')?.value
    }
  }
}
