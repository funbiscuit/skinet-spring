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
  shopParams = new ShopParams();
  totalCount = 0;
  sortOptions = [
    {name: 'Alphabetical', value: 'name'},
    {name: 'Price: Low to High', value: 'price,asc'},
    {name: 'Price: High to Low', value: 'price,desc'},
  ];

  constructor(private shopService: ShopService) {
  }

  ngOnInit(): void {
    this.getProducts()
    this.getBrands()
    this.getTypes()
  }

  getProducts() {
    this.shopService.getProducts(this.shopParams)
      .subscribe(response => {
        if (response != undefined) {
          this.products = response.content
          // page number starts from 0
          this.shopParams.pageNumber = response.number
          this.shopParams.pageSize = response.size
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
    this.shopParams.brandId = brandId
    this.shopParams.pageNumber = 0
    this.getProducts()
  }

  onTypeSelected(typeId: number) {
    this.shopParams.typeId = typeId
    this.shopParams.pageNumber = 0
    this.getProducts()
  }

  onSortSelected(sort: string) {
    this.shopParams.sort = sort
    this.shopParams.pageNumber = 0
    //TODO this doesn't update selected page in pagination
    this.getProducts()
  }

  onPageChanged(page: number) {
    if (this.shopParams.pageNumber !== page - 1) {
      this.shopParams.pageNumber = page - 1;
      this.getProducts()
    }
  }

  onSearch() {
    this.shopParams.search = this.searchTerm.nativeElement.value
    this.shopParams.pageNumber = 0
    this.getProducts()
  }

  onReset() {
    this.searchTerm.nativeElement.value = ''
    this.shopParams = new ShopParams()
    this.shopParams.pageNumber = 0
    this.getProducts()
  }
}
