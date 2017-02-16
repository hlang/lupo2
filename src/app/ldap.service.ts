import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, RequestOptions} from "@angular/http";
import {Observable} from "rxjs";
import "rxjs/add/operator/map";
import {SearchResult} from "./search-result";

@Injectable()
export class LdapService {

    constructor(private http: Http) {
    }

    private personUrl = 'users/search';  // URL to web API


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

        return this.http.get(this.personUrl, options)
            .map(this.extractData);

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
