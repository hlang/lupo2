import {Component} from "@angular/core";
import {AuthService} from "../auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {

    username: string;
    password: string;
    loginError: boolean = false;

    constructor(public authService: AuthService, public router: Router) {
    }

    login() {
        this.authService.login(this.username, this.password)
            .subscribe(() => {
                    if (this.authService.isLoggedIn) {
                        let redirect = this.authService.redirectUrl ? this.authService.redirectUrl : '/';
                        this.router.navigate([redirect]);
                    }
                this.loginError = !this.authService.isLoggedIn;
                }
            );
    }

}