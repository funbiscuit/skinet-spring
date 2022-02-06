import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Product} from "../shared/models/products";
import {ShopService} from "./shop.service";
import {ProductBrand} from "../shared/models/productBrand";
import {ProductType} from "../shared/models/productType";
import {ShopParams} from "../shared/models/shopParams";

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.scss']
})
export class ShopComponent implements OnInit {

  @ViewChild('search', {static: false})
  searchTerm!: ElementRef

  products?: Product[];
  brands: ProductBrand[] = [];
  types: ProductType[] = [];
  shopParams: ShopParams
  totalCount = 0;
  sortOptions = [
    {name: 'Alphabetical', value: 'name'},
    {name: 'Price: Low to High', value: 'price,asc'},
    {name: 'Price: High to Low', value: 'price,desc'},
  ];

  constructor(private shopService: ShopService) {
    this.shopParams = shopService.getShopParams()
  }

  ngOnInit(): void {
    this.getProducts(true)
    this.getBrands()
    this.getTypes()
  }

  getProducts(useCache = false) {
    this.shopService.getProducts(useCache).subscribe(response => {
      if (response != undefined) {
        this.products = response.content
        this.totalCount = response.totalElements
      }
    }, error => {
      console.log(error)
    })
  }

  getBrands() {
    this.shopService.getProductBrands().subscribe(response => {
      this.brands = [{id: 0, name: 'All'}, ...response]
    }, error => {
      console.log(error)
    })
  }

  getTypes() {
    this.shopService.getProductTypes().subscribe(response => {
      this.types = [{id: 0, name: 'All'}, ...response]
    }, error => {
      console.log(error)
    })
  }

  onBrandSelected(brandId: number) {
    const params = this.shopService.getShopParams()
    params.brandId = brandId
    params.pageNumber = 0
    this.shopService.setShopParams(params)
    this.getProducts()
  }

  onTypeSelected(typeId: number) {
    const params = this.shopService.getShopParams()
    params.typeId = typeId
    params.pageNumber = 0
    this.shopService.setShopParams(params)
    this.getProducts()
  }

  onSortSelected(sort: string) {
    const params = this.shopService.getShopParams()
    params.sort = sort
    params.pageNumber = 0
    this.shopService.setShopParams(params)
    this.getProducts()
  }

  onPageChanged(page: number) {
    const params = this.shopService.getShopParams()
    if (params.pageNumber !== page - 1) {
      params.pageNumber = page - 1;
      this.shopService.setShopParams(params)
      this.getProducts(true)
    }
  }

  onSearch() {
    const params = this.shopService.getShopParams()
    params.search = this.searchTerm.nativeElement.value
    params.pageNumber = 0
    this.shopService.setShopParams(params)
    this.getProducts()
  }

  onReset() {
    this.searchTerm.nativeElement.value = ''
    this.shopParams = new ShopParams()
    this.shopService.setShopParams(this.shopParams)
    this.getProducts()
  }
}
