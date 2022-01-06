import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Pagination} from "../shared/models/pagination";
import {ProductBrand} from "../shared/models/productBrand";
import {ProductType} from "../shared/models/productType";
import {map} from "rxjs/operators";
import {ShopParams} from "../shared/models/shopParams";
import {Product} from "../shared/models/products";

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  baseUrl = 'https://localhost:5001/api/'

  constructor(private http: HttpClient) {
  }

  getProducts(shopParams: ShopParams) {
    let params = new HttpParams();

    if (shopParams.brandId !== 0) {
      params = params.append('brandId', shopParams.brandId.toString())
    }
    if (shopParams.typeId !== 0) {
      params = params.append('typeId', shopParams.typeId.toString())
    }
    if (shopParams.search) {
      params = params.append('search', shopParams.search)
    }
    params = params.append('sort', shopParams.sort)

    params = params.append('page', shopParams.pageNumber)
    params = params.append('size', shopParams.pageSize)

    return this.http.get<Pagination>(this.baseUrl + 'products',
      {observe: 'response', params})
      .pipe(
        map(response => {
          return response.body
        })
      )
  }

  getProduct(id: number) {
    return this.http.get<Product>(this.baseUrl + 'products/' + id)
  }

  getProductBrands() {
    return this.http.get<ProductBrand[]>(this.baseUrl + 'products/brands')
  }

  getProductTypes() {
    return this.http.get<ProductType[]>(this.baseUrl + 'products/types')
  }

}
