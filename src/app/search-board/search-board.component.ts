import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {Person} from "../person";
import {LdapService} from "../ldap.service";
import {NotificationService} from "../notification.service";

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

    constructor(private router: Router,
                private ldapService: LdapService,
                private notificationService: NotificationService,) {
    }

    ngOnInit() {
        this.getPersons();
    }

    getPersons(searchStr?: string) {
        this.getPersonsPage(0, searchStr);
    }

    getPersonsPage(pageNumber: number, searchStr?: string): void {
        this.ldapService.getPersonsByAttribute(pageNumber, searchStr)
            .subscribe(searchResult => {
                    this.persons = searchResult.persons;
                    this.pageSize = searchResult.size;
                    this.collectionSize = searchResult.totalElements;
                },
                response => {
                    this.notificationService.notifyServerError('Loading LDAP entries failed! Status: ' + response.status)
                }
            );
    }

    onSelect(person: Person) {
        this.router.navigate(['/detail', person.dn]);

    }
    public pageChanged(event: any): void {
        console.log(event);
        this.pageNumber = event.page;
        this.getPersonsPage(this.pageNumber, this.searchStr);
    };
}
