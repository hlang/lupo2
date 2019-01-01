import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {ArrayObservable} from "rxjs/observable/ArrayObservable";
import "rxjs/add/observable/of";
import "rxjs/add/operator/do";
import "rxjs/add/operator/delay";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {of} from "rxjs";

export class AuthStatus {
    isLoggedIn: boolean = false;
    isAdmin: boolean = false;
    userDn: string;
}

@Injectable()
export class AuthService {
    authStatus: AuthStatus = new AuthStatus();

    constructor(private http: HttpClient) {
    }

    currentAuthStatus(): Observable<AuthStatus> {
        return ArrayObservable.of(this.authStatus)
    }

    authenticate(): Observable<AuthStatus> {
        return this.http.get<any>('api/currentuser')
            .map(
                status => {
                    const authStatus = new AuthStatus();
                    if (status && status.name) {
                        authStatus.isLoggedIn = true;
                        authStatus.isAdmin = status.admin;
                        authStatus.userDn = status.dn;
                    }
                    this.authStatus = authStatus;
                    return authStatus;
                })
            .pipe(catchError(err => of(new AuthStatus())));
    }

    login(username: string, password: string): Observable<AuthStatus> {
        const httpOptions = {
            headers: new HttpHeaders()
                .set('Content-Type', 'application/x-www-form-urlencoded')
        };
        let urlSearchParams = new URLSearchParams();
        urlSearchParams.append('username', username);
        urlSearchParams.append('password', password);
        let body = urlSearchParams.toString();
        return this.http.post<any>('login', body, httpOptions)
            .pipe(catchError(err => of(null)))
            .switchMap(
                () => this.authenticate()
            );
    }

    logout(): void {
        this.authStatus = new AuthStatus();
        this.http.post("logout", {}, {responseType: 'text'})
            .subscribe();

    }
}
