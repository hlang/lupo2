import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ToastOptions, ToastyService} from "ng2-toasty";


import {LdapService} from "../ldap.service";
import {Person} from "../person";
import "rxjs/add/operator/switchMap";

@Component({
    selector: 'app-person-detail',
    templateUrl: './person-detail.component.html',
    styleUrls: ['./person-detail.component.css']
})
export class PersonDetailComponent implements OnInit {
    dn: string;
    person: Person;

    constructor(private router: Router,
                private route: ActivatedRoute,
                private ldapService: LdapService,
                private toastyService: ToastyService,
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
        this.addToast(person);
    }

    addToast(person: Person): void {
        let toastOptions: ToastOptions = {
            title: "Deleted!",
            msg: person.fullName,
            showClose: true,
            timeout: 5000,
        };

        this.toastyService.info(toastOptions);
    }
}
