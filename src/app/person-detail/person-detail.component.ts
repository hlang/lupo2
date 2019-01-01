import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";


import {LdapService} from "../ldap.service";
import {Person} from "../person";
import "rxjs/add/operator/switchMap";
import {AuthService} from "../auth.service";
import {ConfirmationService, MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
    selector: 'app-person-detail',
    templateUrl: './person-detail.component.html',
    styleUrls: ['./person-detail.component.css']
})
export class PersonDetailComponent implements OnInit {

    person: Person = new Person;
    personUpdated: Person = new Person;
    confirmPasswordStr: String;
    passwordEdit: Boolean = false;
    personEdit: Boolean = false;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private ldapService: LdapService,
                private authService: AuthService,
                private confirmationService: ConfirmationService,
                private messageService: MessageService) {
    }

    ngOnInit() {
        this.route.params
            .subscribe((params: Params) => this.loadPerson(params['dn']));
    }

    private loadPerson(dn: string) {
        this.ldapService.getPersonByDn(dn)
            .subscribe((person: Person) => this.person = person,
                error => this.handleError(dn, error)
            );
    }

    isAdmin(): boolean {
        return this.authService.authStatus.isAdmin;
    }

    confirm() {
        this.confirmationService.confirm({
            message: `Delete user ${this.person.fullName}?`,
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.deleteLdap(this.person);
            },
            reject: () => {
            }
        });
    }
    deleteLdap(person: Person) {
        this.ldapService.deletePerson(person.dn)
            .subscribe(
                res => {
                    this.messageService.add(
                        {severity: 'info', summary: person.fullName, detail: 'Deleted!'}
                        );
                    this.router.navigate(['/search'])
                }
            );

    }

    updatePerson(person: Person) {
        this.ldapService.updatePerson(person)
            .subscribe(
                () => {
                    this.personEdit = false;
                    this.loadPerson(person.dn);
                    this.messageService.add(
                        {severity: 'success', summary: person.fullName, detail: 'Updated!'})
                },
                error => this.handleError(person.dn, error)
            );
    }

    savePassword() {
        this.ldapService.setPasswd(this.person)
            .subscribe(() => {
                this.messageService.add(
                    {severity: 'success', summary: this.person.fullName, detail: 'Password updated!'});
                },
                    error => this.handleError(this.person.dn, error)


            );
        this.passwordEdit = false;
    }

    togglePasswordEdit(): void {
        this.passwordEdit = !this.passwordEdit;
        this.resetPassword();
    }


    togglePersonEdit(): void {
        this.personEdit = !this.personEdit;
        this.personUpdated = Person.fromPerson(this.person);
        this.resetPassword();
    }

    private resetPassword(): void {
        this.confirmPasswordStr = "";
        this.person.password = "";
    }

    private handleError(dn: String, error: HttpErrorResponse) : void {
        this.messageService.add(
            {severity: 'error',
                summary: 'Server error!',
                detail: `Accessing DN ${dn} failed! Status: ${error.status}`
            })
    }
}
