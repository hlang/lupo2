import {Component, OnInit} from "@angular/core";
import {ToastOptions, ToastyService} from "ng2-toasty";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {Router} from "@angular/router";

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
                private toastyService: ToastyService,
                private ldapService: LdapService) {
    }

    ngOnInit() {
    }


    newPerson(): void {
        this.ldapService.addPerson(this.person);
        this.addToast();
        this.router.navigate(['/search']);
    }

    addToast(): void {
        let toastOptions: ToastOptions = {
            title: "Created!",
            msg: this.person.fullName,
            showClose: true,
            timeout: 5000
        };

        this.toastyService.success(toastOptions);
    }
}
