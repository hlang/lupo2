import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";
import {Observable} from "rxjs/Observable";

@Injectable()
export class AuthGuardService implements CanActivate {
    constructor(private authService: AuthService,
                private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.authService.authenticate()
            .map(authStatus => {
                let isAdmin = authStatus.isLoggedIn && authStatus.isAdmin;
                if (!isAdmin) {
                    this.router.navigate(['/login']);
                }
                return isAdmin;
            });
    }

}
