import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";


import {LdapService} from "../ldap.service";
import {Person} from "../person";
import "rxjs/add/operator/switchMap";
import {NotificationService} from "../notification.service";
import {AuthService} from "../auth.service";
import {ConfirmationService} from "primeng/api";

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
                private notificationService: NotificationService,
                private authService: AuthService,
                private confirmationService: ConfirmationService,
                private modalService: NgbModal) {
    }

    ngOnInit() {
        this.route.params
            .subscribe((params: Params) => this.loadPerson(params['dn']));
    }

    private loadPerson(dn: string) {
        this.ldapService.getPersonByDn(dn)
            .subscribe((person: Person) => this.person = person,
                response => this.notificationService.notifyServerError('Loading DN ' + dn + ' failed! Status: ' + response.status)
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
                res => this.router.navigate(['/search'])
            );
        this.notificationService.notify(
            {
                severity: 'info',
                summary: person.fullName,
                detail: 'Deleted!'
            });
    }

    updatePerson(person: Person) {
        this.ldapService.updatePerson(person)
            .subscribe(
                () => {
                    this.personEdit = false;
                    this.loadPerson(person.dn);
                    this.notificationService.notify(
                        {
                            severity: 'success',
                            summary: person.fullName,
                            detail: 'Updated!'
                        })
                }
            );
    }

    savePassword() {
        this.ldapService.setPasswd(this.person);
        this.notificationService.notify(
            {
                severity: 'success',
                summary: this.person.fullName,
                detail: 'Password updated!'
            });
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
}
