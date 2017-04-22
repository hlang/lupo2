import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService, AuthStatus} from "./auth.service";
import {Observable} from "rxjs/Observable";

@Injectable()
export class AdminUserGuardService implements CanActivate {
    constructor(private authService: AuthService,
                private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.authService.authenticate()
            .map(authStatus => {
                let isAdmin = authStatus.isLoggedIn && authStatus.isAdmin;
                if (isAdmin || AdminUserGuardService.isUserDn(authStatus, route)) {
                    return true;
                }
                this.router.navigate(['/login']);
                return false;
            });
    }

    private static isUserDn(authStatus: AuthStatus, route: ActivatedRouteSnapshot): boolean {
        let userCn = route.paramMap.get('dn');
        return (userCn != null)
            && (authStatus.userDn != null)
            && authStatus.userDn.startsWith(decodeURIComponent(userCn));
    }
}
