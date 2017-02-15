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
    pageNumber = 1;
    pageSize = 10;
    collectionSize = 0;

    constructor(private ldapService: LdapService) {
    }

    ngOnInit() {
        this.getPersons(1);
    }

    getPersons(pageNumber: number, searchStr?: string): void {
        this.ldapService.getPersonsByAttribute(pageNumber, searchStr)
            .subscribe(searchResult => {
                    this.persons = searchResult.persons;
                    this.pageNumber = searchResult.number;
                    this.pageSize = searchResult.size;
                    this.collectionSize = searchResult.totalElements;
                }
            );
    }

    public pageChanged(page: any): void {
        this.getPersons(this.pageNumber, this.searchStr);
    };
}
