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
    message: string;

    constructor(public authService: AuthService, public router: Router) {
        this.setMessage();
    }

    setMessage() {
        this.message = 'Logged ' + (this.authService.isLoggedIn ? 'in' : 'out');
    }

    login() {
        this.message = 'Trying to log in ...';
        this.authService.login(this.username, this.password)
            .subscribe(() => {
                    this.setMessage();
                    if (this.authService.isLoggedIn) {
                        let redirect = this.authService.redirectUrl ? this.authService.redirectUrl : '/';
                        this.router.navigate([redirect]);
                    }
                }
            );
    }

}
