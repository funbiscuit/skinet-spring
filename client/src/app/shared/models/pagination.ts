import {Product} from "./products";

export interface Pagination {
  number: number;
  size: number;
  numberOfElements: number;
  totalPages: number;
  totalElements: number;

  content: Product[];
}
