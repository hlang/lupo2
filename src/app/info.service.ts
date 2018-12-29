import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class InfoService {


    constructor(private http: HttpClient) {
    }

    private infoUrl = 'actuator/info';


    getInfoVersion(): Observable<string> {
        return this.http.get<any>(this.infoUrl)
            .map(res => res.build.version);
    }
}
