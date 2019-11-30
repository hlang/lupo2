import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import "rxjs/add/operator/map";
import {SearchResult} from "./search-result";
import {Person} from "./person";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable()
export class LdapService {

    constructor(private http: HttpClient) {
    }

    private searchUrl = 'api/users/search';
    private userUrl = 'api/users/';


    getPersonByDn(dn: string): Observable<Person> {
        return this.http.get<Person>(this.userUrl + dn);
    }

    getPersonsByAttribute(pageNumber: number, searchStr?: string,): Observable<SearchResult> {
        let params = new HttpParams();
        if (searchStr) {
            params = params
                .set('uid', searchStr)
                .set('firstname', searchStr)
                .set('lastname', searchStr)
                .set('email', searchStr);
        }
        params = params
            .set('number', String(pageNumber))
            .set('size', '20');
        const options = {params: params};
        return this.http.get(this.searchUrl, options)
            .map(body => this.extractData(body));

    }

    deletePerson(dn: string): Observable<Object> {
        return this.http.delete(this.userUrl + dn);
    }

    addPerson(person: Person): Observable<Object> {
        return this.http.post<Person>(this.userUrl, person);
    }

    updatePerson(person: Person): Observable<Object> {
        return this.http.put<Person>(this.userUrl, person);
    }

    setPasswd(person: Person): Observable<Object> {
        return this.http.put(this.userUrl + "passwd", person);
    }

    getByUid(uid: string) : Observable<Person> {
        return this.http.get<Person>(this.userUrl + `uid/${uid}`);
    }

    private extractData(body: any) {
        const searchResult = new SearchResult();
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
