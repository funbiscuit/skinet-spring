import {Component, OnInit} from '@angular/core';
import {AsyncValidatorFn, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AccountService} from "../account.service";
import {Router} from "@angular/router";
import {of, timer} from "rxjs";
import {map, switchMap} from "rxjs/operators";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup
  errors: any

  constructor(private fb: FormBuilder,
              private accountService: AccountService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.createRegisterForm()
  }

  createRegisterForm() {
    this.registerForm = this.fb.group({
      name: [null, [Validators.required]],
      email: [null,
        [Validators.required, Validators.email],
        [this.validateEmailNotTaken()]],
      password: [null, [Validators.required]],
    })
  }

  onSubmit() {
    this.accountService.register(this.registerForm.value).subscribe(response => {
      this.router.navigateByUrl('/shop')
    }, error => {
      this.errors = []
      this.errors = Object.keys(error.errors).map((key) =>
        key + ' ' + error.errors[key]
      )
      console.log(error)
    })
  }

  validateEmailNotTaken(): AsyncValidatorFn {
    return control => {
      return timer(500).pipe(
        switchMap(() => {
          if (!control.value) {
            return of(null)
          }
          return this.accountService.checkEmailExists(control.value).pipe(
            map(res => {
              return res ? {emailExists: true} : null
            })
          )
        })
      )
    }
  }
}
