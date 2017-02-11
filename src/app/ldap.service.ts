import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, RequestOptions} from "@angular/http";
import {Observable} from "rxjs";
import {Person} from "./person";
import "rxjs/add/operator/map";

@Injectable()
export class LdapService {

    constructor(private http: Http) {
    }

    private personUrl = 'users/search';  // URL to web API

    getPersons(): Observable<Person[]> {
        return this.getPersonsByUid();
    }

    getPersonsByUid(uid?: string): Observable<Person[]> {
        let params: URLSearchParams = new URLSearchParams();
        if (uid) {
            params.set('uid', uid);
        }
        let options = new RequestOptions();
        options.search = params;

        return this.http.get(this.personUrl, options)
            .map(this.extractData);

    }

    private extractData(res: Response) {
        let body = res.json();
        return body || {};
    }
}
