import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";

@Injectable()
export class AdminUserGuardService implements CanActivate {
    constructor(private authService: AuthService) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.authService.isLoggedIn &&
            (this.authService.isAdmin || this.isUserDn(route));
    }

    isUserDn(route: ActivatedRouteSnapshot): boolean {
        let userCn = route.paramMap.get('dn');
        return (userCn != null)
            && (this.authService.userDn != null)
            && this.authService.userDn.startsWith(decodeURIComponent(userCn));
    }
}
