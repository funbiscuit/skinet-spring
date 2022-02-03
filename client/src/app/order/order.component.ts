import {Component, OnInit} from '@angular/core';
import {OrderService} from "./order.service";
import {Order} from "../shared/models/order";
import {Router} from "@angular/router";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {

  orders: Order[] = []

  constructor(private orderService: OrderService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.orderService.getOrders().subscribe(orders => {
      this.orders = orders
    })
  }

  onOrderSelected(order: Order) {
    this.router.navigateByUrl(`/orders/${order.id}`)
  }
}
