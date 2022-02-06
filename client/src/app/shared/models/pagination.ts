import {Product} from "./products";

export class Pagination {
  number = 0;
  size = 0;
  numberOfElements = 0;
  totalPages = 0;
  totalElements = 0;

  content: Product[] = [];
}
