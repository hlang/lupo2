import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
                private modalService: NgbModal) {
    }

    ngOnInit() {
        this.route.params
            .switchMap((params: Params) => this.ldapService.getPersonByDn(params['dn']))
            .subscribe((person: Person) => this.person = person);
    }

    open(content, dn) {
        this.modalService.open(content).result.then((result) => {
            if (result) {
                this.deleteLdap(dn);
            }
        }, (reason) => {
            ;
        });
    }

    deleteLdap(dn: string) {
        this.ldapService.deletePerson(dn)
            .subscribe(
                res => this.router.navigate(['/search'])
            );
    }

}
