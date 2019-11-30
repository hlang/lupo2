import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {MessageService} from "primeng/api";
import {HttpErrorResponse} from "@angular/common/http";
import {Subject} from "rxjs";
import {debounceTime, distinctUntilChanged} from "rxjs/operators";

@Component({
    selector: 'app-search-board',
    templateUrl: './search-board.component.html',
    styleUrls: ['./search-board.component.css']
})
export class SearchBoardComponent implements OnInit {
    searchStr: string;
    persons: Person[];
    pageNumber = 0;
    pageSize = 10;
    collectionSize = 0;
    private searchStrSource = new Subject<string>();

    constructor(private router: Router,
                private ldapService: LdapService,
                private messageService: MessageService) {
    }

    ngOnInit() {
        this.getPersons();
        this.searchStrSource.asObservable()
            .pipe(
                debounceTime(500),
                distinctUntilChanged(),
            )
            .subscribe(
                searchStr => this.getPersons()
            )
    }

    searchStrChange() {
        this.searchStrSource.next(this.searchStr);
    }

    getPersons() {
        this.getPersonsPage(0, this.searchStr);
    }

    getPersonsPage(pageNumber: number, searchStr?: string): void {
        this.ldapService.getPersonsByAttribute(pageNumber, searchStr)
            .subscribe(searchResult => {
                    this.persons = searchResult.persons;
                    this.pageSize = searchResult.size;
                    this.collectionSize = searchResult.totalElements;
                },
                error => this.handleError(error)
            );
    }

    onSelect(person: Person) {
        this.router.navigate(['/detail', person.dn]);

    }
    public pageChanged(event: any): void {
        this.pageNumber = event.page;
        this.getPersonsPage(this.pageNumber, this.searchStr);
    };

    private handleError(error: HttpErrorResponse) : void {
        this.messageService.add(
            {severity: 'error',
                summary: 'Server error!',
                detail: `Loading LDAP entries failed! Status: ${error.status}`
            })
    }

}
