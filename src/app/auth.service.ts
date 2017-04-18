import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Headers, Http, RequestOptions} from "@angular/http";
import "rxjs/add/observable/of";
import "rxjs/add/operator/do";
import "rxjs/add/operator/delay";

@Injectable()
export class AuthService {
    isLoggedIn: boolean = false;
    redirectUrl: string;

    constructor(private http: Http) {
        this.authenticate()
            .subscribe();
    }

    authenticate(): Observable<boolean> {
        return this.http.get('api/currentuser')
            .map(
                response => {
                    if (response.json().name) {
                        this.isLoggedIn = true;
                        return true
                    } else {
                        this.isLoggedIn = false;
                        return false
                    }
                }
            );
    }

    login(username: string, password: string): Observable<boolean> {
        let headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', username);
        urlSearchParams.append('password', password);
        let body = urlSearchParams.toString();
        let options = new RequestOptions({headers: headers});
        return this.http.post('login', body, options)
            .switchMap(() => this.authenticate());
    }

    logout(): void {
        this.http.post("logout", {})
            .subscribe(
                data => this.isLoggedIn = false,
                error => this.isLoggedIn = false,
                () => this.isLoggedIn = false
            );

    }
}
