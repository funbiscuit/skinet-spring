import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormGroup} from "@angular/forms";
import {AccountService} from "../../account/account.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-checkout-address',
  templateUrl: './checkout-address.component.html',
  styleUrls: ['./checkout-address.component.scss']
})
export class CheckoutAddressComponent implements OnInit {

  @Input() checkoutForm!: FormGroup

  addressForm!: AbstractControl

  constructor(private accountService: AccountService,
              private toastr: ToastrService) {
  }

  ngOnInit(): void {
    const form = this.checkoutForm.get('addressForm')
    if (form) {
      this.addressForm = form
    }
  }

  saveUserAddress() {
    this.accountService.updateUserAddress(this.addressForm.value)
      .subscribe(address => {
        this.toastr.success('Address saved')
        this.addressForm.reset(address)
      }, error => {
        this.toastr.error(error.message)
        console.log(error)
      })
  }
}
