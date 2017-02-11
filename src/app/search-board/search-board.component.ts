import {Component, OnInit} from "@angular/core";
import {Person} from "../person";
import {LdapService} from "../ldap.service";

@Component({
    selector: 'app-search-board',
    templateUrl: './search-board.component.html',
    styleUrls: ['./search-board.component.css']
})
export class SearchBoardComponent implements OnInit {
    searchUid: string;
    persons: Person[];

    constructor(private ldapService: LdapService) {
    }

    ngOnInit() {
        this.getPersons();
    }

    getPersons(uid?: string): void {
        this.ldapService.getPersonsByUid(uid)
            .subscribe(persons => this.persons = persons);
    }
}
