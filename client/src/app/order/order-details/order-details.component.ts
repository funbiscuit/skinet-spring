import {Component, OnInit} from '@angular/core';
import {BreadcrumbService} from "xng-breadcrumb";
import {Order} from "../../shared/models/order";
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../order.service";

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit {

  order?: Order

  constructor(private activatedRoute: ActivatedRoute,
              private orderService: OrderService,
              private bcService: BreadcrumbService) {
    this.bcService.set('@orderDetails', ' ')
  }

  ngOnInit(): void {
    this.loadOrder()
  }

  private loadOrder() {
    const id = this.activatedRoute.snapshot.paramMap.get('id')

    if (id) {
      this.orderService.getOrder(+id).subscribe(order => {
        this.order = order
        this.bcService.set('@orderDetails', 'Order #' + order.id + ' - ' + order.status)
      }, error => {
        console.log(error)
      })
    }
  }
}
