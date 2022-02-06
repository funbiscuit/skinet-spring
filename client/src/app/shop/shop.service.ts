import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Pagination} from "../shared/models/pagination";
import {ProductBrand} from "../shared/models/productBrand";
import {ProductType} from "../shared/models/productType";
import {map} from "rxjs/operators";
import {ShopParams} from "../shared/models/shopParams";
import {Product} from "../shared/models/products";
import {of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  baseUrl = 'https://localhost:5001/api/'

  products: Product[] = []
  brands: ProductBrand[] = []
  types: ProductType[] = []
  pagination: Pagination = new Pagination()
  shopParams = new ShopParams()
  productCache = new Map<string, Product[]>();

  constructor(private http: HttpClient) {
  }

  getProducts(useCache: boolean) {
    if (!useCache) {
      this.productCache = new Map();
    }

    const key = Object.values(this.shopParams).join('-');
    if (this.productCache.has(key)) {
      this.pagination.content = this.productCache.get(key)!
      return of(this.pagination)
    }

    let params = new HttpParams();

    if (this.shopParams.brandId !== 0) {
      params = params.append('brandId', this.shopParams.brandId.toString())
    }
    if (this.shopParams.typeId !== 0) {
      params = params.append('typeId', this.shopParams.typeId.toString())
    }
    if (this.shopParams.search) {
      params = params.append('search', this.shopParams.search)
    }
    params = params.append('sort', this.shopParams.sort)

    params = params.append('page', this.shopParams.pageNumber)
    params = params.append('size', this.shopParams.pageSize)

    return this.http.get<Pagination>(this.baseUrl + 'products',
      {observe: 'response', params})
      .pipe(
        map(response => {
          if (response.body?.content != null) {
            this.productCache.set(key, response.body.content)
            this.pagination = response.body
          }
          return response.body
        })
      )
  }

  setShopParams(shopParams: ShopParams) {
    this.shopParams = shopParams
  }

  getShopParams() {
    return this.shopParams
  }

  getProduct(id: number) {
    let product: Product | undefined
    this.productCache.forEach(products => {
      product = product ?? products.find(p => p.id === id)
    })

    if (product) {
      return of(product)
    }
    return this.http.get<Product>(this.baseUrl + 'products/' + id)
  }

  getProductBrands() {
    if (this.brands.length > 0) {
      return of(this.brands);
    }
    return this.http.get<ProductBrand[]>(this.baseUrl + 'products/brands').pipe(
      map(response => {
        this.brands = response
        return response
      })
    )
  }

  getProductTypes() {
    if (this.types.length > 0) {
      return of(this.types);
    }
    return this.http.get<ProductType[]>(this.baseUrl + 'products/types').pipe(
      map(response => {
          this.types = response
          return response
        }
      )
    )
  }

}
