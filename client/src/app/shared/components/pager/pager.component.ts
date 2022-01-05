import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-pager',
  templateUrl: './pager.component.html',
  styleUrls: ['./pager.component.scss']
})
export class PagerComponent implements OnInit {
  @Input()
  totalCount = 0;

  @Input()
  pageSize = 0;

  @Output()
  pageChanged = new EventEmitter<number>();

  constructor() {
  }

  ngOnInit(): void {
  }

  onPagerChange(page: number) {
    this.pageChanged.emit(page)
  }
}
