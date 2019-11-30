import {Component, OnInit} from "@angular/core";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {debounceTime} from "rxjs/operators";

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
@Component({
    selector: 'app-person-create',
    templateUrl: './person-create.component.html',
    styleUrls: ['./person-create.component.css']
})
export class PersonCreateComponent implements OnInit {
    person = new AddPerson();
    private inputSource = new Subject<void >();
    uidAlreadyFound: boolean = false;

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

    private handleError(person: Person, error: HttpErrorResponse) : void {
        this.messageService.add(
            {severity: 'error',
                summary: 'Server error!',
                detail: `Adding Person ${person.uid} failed! Status: ${error.status}`
            })
    }

    private resetValues() {
        this.person = new AddPerson();
    }

}
