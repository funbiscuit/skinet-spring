import {AfterViewInit, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormGroup} from "@angular/forms";
import {BasketService} from "../../basket/basket.service";
import {CheckoutService} from "../checkout.service";
import {ToastrService} from "ngx-toastr";
import {IBasket} from "../../shared/models/basket";
import {NavigationExtras, Router} from "@angular/router";
import {of} from "rxjs";

declare var Stripe: any;

@Component({
  selector: 'app-checkout-payment',
  templateUrl: './checkout-payment.component.html',
  styleUrls: ['./checkout-payment.component.scss']
})
export class CheckoutPaymentComponent implements OnInit, AfterViewInit, OnDestroy {
  @Input() checkoutForm!: FormGroup
  @ViewChild('cardNumber', {static: true}) cardNumberElement!: ElementRef
  @ViewChild('cardExpiry', {static: true}) cardExpiryElement!: ElementRef
  @ViewChild('cardCvc', {static: true}) cardCvcElement!: ElementRef

  stripeEnabled = false
  stripe: any
  cardNumber: any
  cardExpiry: any
  cardCvc: any
  cardErrors: any
  cardHandler = this.onChange.bind(this)

  cardNumberValid = false
  cardExpiryValid = false
  cardCvcValid = false

  loading = false

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

  ngAfterViewInit(): void {
    this.checkoutService.stripeConfig$.subscribe(config => {
      this.stripeEnabled = config.publishableKey != "pk_test_YOUR_KEY"

      if (this.stripeEnabled) {
        this.stripe = Stripe(config.publishableKey)
        const elements = this.stripe.elements()

        this.cardNumber = elements.create('cardNumber')
        this.cardNumber.mount(this.cardNumberElement.nativeElement)
        this.cardNumber.addEventListener('change', this.cardHandler)

        this.cardExpiry = elements.create('cardExpiry')
        this.cardExpiry.mount(this.cardExpiryElement.nativeElement)
        this.cardExpiry.addEventListener('change', this.cardHandler)

        this.cardCvc = elements.create('cardCvc')
        this.cardCvc.mount(this.cardCvcElement.nativeElement)
        this.cardCvc.addEventListener('change', this.cardHandler)
      } else {
        console.log('Stripe key is: ' + config.publishableKey)
        console.log('Stripe is disabled. Order status will be based on shipping country')
        this.cardNumberValid = true
        this.cardExpiryValid = true
        this.cardCvcValid = true
      }
    })
  }

  ngOnDestroy(): void {
    if (this.stripeEnabled) {
      this.cardNumber.destroy()
      this.cardExpiry.destroy()
      this.cardCvc.destroy()
    }
  }

  onChange(event: any) {
    console.log(event)
    if (event.error) {
      this.cardErrors = event.error.message
    } else {
      this.cardErrors = null
    }
    switch (event.elementType) {
      case 'cardNumber':
        this.cardNumberValid = event.complete
        break
      case 'cardExpiry':
        this.cardExpiryValid = event.complete
        break
      case 'cardCvc':
        this.cardCvcValid = event.complete
        break
    }
  }

  async submitOrder() {
    this.loading = true
    const basket = this.basketService.getCurrentBasketValue()

    try {
      const createdOrder = await this.createOrder(basket)
      const paymentResult = await this.confirmPaymentWithStripe(basket)

      if (paymentResult.paymentIntent) {
        this.basketService.deleteBasket(basket)
        const navigationExtras: NavigationExtras = {state: createdOrder}
        this.router.navigate(['checkout/success'], navigationExtras)
      } else {
        this.toastr.error(paymentResult.error.message)
      }
    } catch (error) {
      console.log(error)
    } finally {
      this.loading = false
    }


  }

  private getOrderToCreate(basket: IBasket) {
    return {
      basketId: basket.id,
      deliveryMethodId: this.deliveryForm.get('deliveryMethod')?.value,
      shippingAddress: this.checkoutForm.get('addressForm')?.value
    }
  }

  private async createOrder(basket: IBasket) {
    const order = this.getOrderToCreate(basket)

    return this.checkoutService.createOrder(order).toPromise()
  }

  private async confirmPaymentWithStripe(basket: IBasket) {
    if (this.stripeEnabled) {
      return this.stripe.confirmCardPayment(basket.clientSecret, {
        payment_method: {
          card: this.cardNumber,
          billing_details: {
            name: this.checkoutForm.get('paymentForm')?.get('nameOnCard')?.value
          }
        }
      })
    } else {
      switch (this.checkoutForm.get('addressForm')?.value.country) {
        case 'UK':
          return of({
            paymentIntent: null,
            error: {
              message: 'Can\'t pay for order to UK. ' +
                'Choose USA for success or other country for Pending'
            }
          }).toPromise()
        default:
          return of({paymentIntent: 'ok'}).toPromise()
      }
    }

  }
}
