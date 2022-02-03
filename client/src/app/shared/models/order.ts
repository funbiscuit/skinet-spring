import {Address} from "./address";

export interface OrderToCreate {
  basketId: string
  deliveryMethodId: number
  shippingAddress: Address
}

export interface OrderItem {
  productId: number
  productName: string
  pictureUrl: string
  price: number
  quantity: number
}

export interface Order {
  id: number
  buyerEmail: string
  orderDate: string
  shippingAddress: Address
  deliveryMethod: string
  shippingPrice: number
  orderItems: OrderItem[]
  subtotal: number
  total: number
  status: string
}
