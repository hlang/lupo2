import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class InfoService {

    constructor(private http: Http) {
    }

    private infoUrl = 'info/';


    getInfoVersion(): Observable<string> {
        return this.http.get(this.infoUrl)
            .map(res => res.json().build.version);
    }
}
