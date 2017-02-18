import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Params} from "@angular/router";
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

    constructor(private route: ActivatedRoute,
                private ldapService: LdapService) {
    }

    ngOnInit() {
        this.route.params
            .switchMap((params: Params) => this.ldapService.getPersonByDn(params['dn']))
            .subscribe((person: Person) => this.person = person);
    }

}
