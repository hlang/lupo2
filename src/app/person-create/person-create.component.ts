import {Component, OnInit} from "@angular/core";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";

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

    constructor(private router: Router,
                private ldapService: LdapService,
                private messageService: MessageService) {
    }

    ngOnInit() {
    }


    newPerson(): void {
        this.ldapService.addPerson(this.person)
            .subscribe(response => {
                    this.addToast();
                    this.router.navigate(['/search']);
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

    private handleError(person: Person, error: HttpErrorResponse) : void {
        this.messageService.add(
            {severity: 'error',
                summary: 'Server error!',
                detail: `Adding Person ${person.uid} failed! Status: ${error.status}`
            })
    }
}
