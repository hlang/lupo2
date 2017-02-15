import {Component, OnInit} from "@angular/core";
import {Person} from "../person";
import {LdapService} from "../ldap.service";

@Component({
    selector: 'app-search-board',
    templateUrl: './search-board.component.html',
    styleUrls: ['./search-board.component.css']
})
export class SearchBoardComponent implements OnInit {
    searchStr: string;
    persons: Person[];

    constructor(private ldapService: LdapService) {
    }

    ngOnInit() {
        this.getPersons();
    }

    getPersons(searchStr?: string): void {
        this.ldapService.getPersonsByAttribute(searchStr)
            .subscribe(persons => this.persons = persons);
    }
}
