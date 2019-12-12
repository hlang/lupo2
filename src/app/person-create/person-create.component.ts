import {Component, OnInit} from "@angular/core";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {catchError, debounceTime} from "rxjs/operators";
import {
    AbstractControl,
    AsyncValidatorFn,
    FormControl,
    FormGroup,
    ValidationErrors,
    ValidatorFn,
    Validators
} from "@angular/forms";

class AddPerson extends Person {
    confirmPassword: string;

    get addFirstName(): string {
        return this.firstName;
    }

    set addFirstName(firstName: string) {
        this.firstName = firstName;
        this.setFullName();
    }

    get addLastName(): string {
        return this.lastName;
    }

    set addLastName(lastName: string) {
        this.lastName = lastName;
        this.setFullName();
    }

    private setFullName(): void {
        let names: string[] = [];
        if (this.firstName) {
            names.push(this.firstName);
        }
        if (this.lastName) {
            names.push(this.lastName);
        }
        this.fullName = names.join(" ");
    }
}

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
    person = new AddPerson();
    private inputSource = new Subject<void >();
    uidAlreadyFound: boolean = false;

    personForm = new FormGroup({
        firstName: new FormControl(''),
        lastName: new FormControl('', Validators.required),
        email: new FormControl('', Validators.email),
        uid: new FormControl('',
            [Validators.required,Validators.minLength(3)],
            this.uidDoesNotExistValidator()),
        password: new FormControl('', Validators.required),
        confirmPassword: new FormControl('', Validators.required)
    },{validators: passwordMatchValidator});

    constructor(private router: Router,
                private ldapService: LdapService,
                private messageService: MessageService) {
    }

    ngOnInit() {
        this.inputSource.asObservable()
            .pipe(
                debounceTime(500)
            )
            .subscribe(
                event => {
                    this.ldapService.getByUid(this.person.uid)
                    .subscribe(
                        person => this.uidAlreadyFound = true,
                        error => this.uidAlreadyFound = false
                    )
                }
            )
    }


    newPerson(): void {
        console.warn(this.personForm.value);
        this.ldapService.addPerson(this.person)
            .subscribe(response => {
                    this.addToast();
                    this.resetValues();
                },
                error => this.handleError(this.person, error)
            )
    }

    addToast(): void {
        this.messageService.add(
            {
                severity: 'success',
                summary: this.person.fullName,
                detail: 'Created!'
            });
    }

    inputChanged() {
        this.inputSource.next(null);
    }

    resetValues() {
        this.person = new AddPerson();
        this.personForm.reset();
    }

    uidDoesNotExistValidator(): AsyncValidatorFn {
        return (
            control: AbstractControl
        ):
            | Promise<{ [key: string]: any } | null>
            | Observable<{ [key: string]: any } | null> => {
            console.info(`Check uid ${control.value}`);
            return this.ldapService.getByUid(control.value)
                .map(person => {
                    const error: ValidationErrors = {'uidExists': true};
                    return error;
                })
                .pipe(catchError(err => Observable.of(null)));
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
