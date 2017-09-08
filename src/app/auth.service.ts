import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {ArrayObservable} from "rxjs/observable/ArrayObservable";
import {Headers, Http, RequestOptions} from "@angular/http";
import "rxjs/add/observable/of";
import "rxjs/add/operator/do";
import "rxjs/add/operator/delay";

export class AuthStatus {
    isLoggedIn: boolean = false;
    isAdmin: boolean = false;
    userDn: string;
}

@Injectable()
export class AuthService {
    authStatus: AuthStatus = new AuthStatus();

    constructor(private http: Http) {
    }

    currentAuthStatus(): Observable<AuthStatus> {
        return ArrayObservable.of(this.authStatus)
    }

    authenticate(): Observable<AuthStatus> {
        return this.http.get('api/currentuser')
            .map(
                response => {
                    let status = new AuthStatus();
                    if (AuthService.isJsonResponse(response) && response.json().name) {
                        let body = response.json();
                        if (body.name) {
                            status.isLoggedIn = true;
                            status.isAdmin = body.admin;
                            status.userDn = body.dn;
                        }
                    }
                    this.authStatus = status;
                    return status;
                }
            );
    }

    private static isJsonResponse(response): boolean {
        return response.headers.get('content-type').includes("application/json");
    }

    login(username: string, password: string): Observable<AuthStatus> {
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
        this.authStatus = new AuthStatus();
        this.http.post("logout", {})
            .subscribe();

    }
}
