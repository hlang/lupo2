import {Component, OnInit} from "@angular/core";
import {BootstrapAlertType, BootstrapGrowlService} from "ng2-bootstrap-growl";
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
                private bootstrapGrowlService: BootstrapGrowlService,
                private ldapService: LdapService) {
    }

    ngOnInit() {
    }


    newPerson(): void {
        this.ldapService.addPerson(this.person);
        this.bootstrapGrowlService.addAlert(this.person.fullName + " created!", BootstrapAlertType.SUCCESS);
        this.router.navigate(['/search']);
    }
}
