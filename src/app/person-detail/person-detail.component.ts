import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";


import {LdapService} from "../ldap.service";
import {Person} from "../person";
import "rxjs/add/operator/switchMap";
import {NotificationService} from "../notification.service";

@Component({
    selector: 'app-person-detail',
    templateUrl: './person-detail.component.html',
    styleUrls: ['./person-detail.component.css']
})
export class PersonDetailComponent implements OnInit {
    dn: string;
    person: Person;
    confirmPasswordStr: String;
    passwordEdit: Boolean = false;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private ldapService: LdapService,
                private notificationService: NotificationService,
                private modalService: NgbModal) {
    }

    ngOnInit() {
        this.route.params
            .switchMap((params: Params) => this.ldapService.getPersonByDn(params['dn']))
            .subscribe((person: Person) => this.person = person);
    }

    open(content, person) {
        this.modalService.open(content).result.then((result) => {
            if (result) {
                this.deleteLdap(person);
            }
        }, (reason) => {
            ;
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

    private resetPassword(): void {
        this.confirmPasswordStr = "";
        this.person.password = "";
    }
}
