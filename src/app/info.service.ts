import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";

export class ActuatorInfo {
    app: App;
}

export class App {
    version: string
}

@Injectable()
export class InfoService {


    constructor(private http: HttpClient) {
    }

    private infoUrl = 'actuator/info';


    getInfoVersion(): Observable<string> {
        return this.http.get<ActuatorInfo>(this.infoUrl)
            .map(res => res.app.version);
    }
}
