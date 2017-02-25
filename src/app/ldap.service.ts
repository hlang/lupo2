import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, RequestOptions} from "@angular/http";
import {Observable} from "rxjs";
import "rxjs/add/operator/map";
import {SearchResult} from "./search-result";
import {Person} from "./person";

@Injectable()
export class LdapService {

    constructor(private http: Http) {
    }

    private searchUrl = 'api/users/search';
    private userUrl = 'api/users/';


    getPersonByDn(dn: string): Observable<Person> {
        return this.http.get(this.userUrl + dn)
            .map(res => <Person>res.json());


    }

    getPersonsByAttribute(pageNumber: number, searchStr?: string,): Observable<SearchResult> {
        let params: URLSearchParams = new URLSearchParams();
        if (searchStr) {
            params.set('uid', searchStr);
            params.set('firstname', searchStr);
            params.set('lastname', searchStr);
            params.set('email', searchStr);
        }
        params.set('number', String(pageNumber - 1));
        params.set('size', '20');
        let options = new RequestOptions();
        options.search = params;

        return this.http.get(this.searchUrl, options)
            .map(this.extractData);

    }

    deletePerson(dn: string): Observable<Response> {
        return this.http.delete(this.userUrl + dn);
    }

    addPerson(person: Person): void {
        this.http.post(this.userUrl, person).subscribe();
    }

    private extractData(res: Response) {
        let body = res.json();
        let searchResult = new SearchResult();
        if (body._embedded) {
            searchResult.persons = body._embedded.userList;
            searchResult.totalElements = body.page.totalElements;
            searchResult.size = body.page.size;
            searchResult.totalPages = body.page.totalPages;
            searchResult.number = body.page.number + 1;
        } else {
            searchResult.totalElements = 0;
        }

        return searchResult;
    }
}
