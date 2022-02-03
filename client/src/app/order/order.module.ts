import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {OrderComponent} from './order.component';
import {OrderRoutingModule} from "./order-routing.module";
import {OrderDetailsComponent} from './order-details/order-details.component';


@NgModule({
  declarations: [
    OrderComponent,
    OrderDetailsComponent
  ],
  imports: [
    CommonModule,
    OrderRoutingModule
  ]
})
export class OrderModule {
}
