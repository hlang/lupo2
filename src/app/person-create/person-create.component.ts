import {Component, OnInit} from "@angular/core";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {
    AbstractControl,
    AsyncValidatorFn,
    FormControl,
    FormGroup,
    ValidationErrors,
    ValidatorFn,
    Validators
} from "@angular/forms";

const passwordMatchValidator : ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    const error = password && confirmPassword &&
            password.value != confirmPassword.value ? { 'passwordMatch': false } : null;
    confirmPassword.setErrors(error);
    return error;
};



@Component({
    selector: 'app-person-create',
    templateUrl: './person-create.component.html',
    styleUrls: ['./person-create.component.css']
})
export class PersonCreateComponent implements OnInit {
    fullName : string = "";
    uidAlreadyFound: boolean = false;

    personForm = new FormGroup({
        firstName: new FormControl(null),
        lastName: new FormControl('', Validators.required),
        email: new FormControl(null, Validators.email),
        uid: new FormControl('',
            [Validators.required,Validators.minLength(3),Validators.pattern('[a-z0-9]*')],
            this.uidDoesNotExistValidator()),
        password: new FormControl('', Validators.required),
        confirmPassword: new FormControl('', Validators.required)
    },{validators: passwordMatchValidator});

    constructor(private router: Router,
                private ldapService: LdapService,
                private messageService: MessageService) {
    }

    ngOnInit() {
        this.personForm.valueChanges
            .subscribe(value => {
                let names: string[] = [];
                if (value.firstName) {
                    names.push(value.firstName);
                }
                if (value.lastName) {
                    names.push(value.lastName);
                }
                this.fullName = names.join(" ");
                }
            )
    }

    get uid() {
        return this.personForm.get('uid');
    }

    newPerson(): void {
        const person : Person = {
            uid : this.personForm.get('uid').value,
            fullName : this.fullName,
            email : this.personForm.get('email').value,
            firstName: this.personForm.get('firstName').value,
            lastName: this.personForm.get('lastName').value,
            password : this.personForm.get('password').value,
            dn : ''
        };
        this.ldapService.addPerson(person)
            .subscribe(_ => {
                    this.addToast(person);
                    this.resetValues();
                },
                error => this.handleError(person, error)
            )
    }

    addToast(person: Person): void {
        this.messageService.add(
            {
                severity: 'success',
                summary: person.fullName,
                detail: 'Created!'
            });
    }

    resetValues() {
        this.fullName = "";
        this.personForm.reset();
    }

    uidDoesNotExistValidator(): AsyncValidatorFn {
        return (
            control: AbstractControl
        ):
            | Promise<{ [key: string]: any } | null>
            | Observable<{ [key: string]: any } | null> => {
            return this.ldapService.getByUid(control.value)
                .map(_ => {
                    const error: ValidationErrors = {'uidExists': true};
                    return error;
                })
                .pipe(catchError(_ => Observable.of(null)));
        }
    }

    private handleError(person: Person, error: HttpErrorResponse) : void {
        this.messageService.add(
            {severity: 'error',
                summary: 'Server error!',
                detail: `Adding Person ${person.uid} failed! Status: ${error.status}`
            })
    }

}
