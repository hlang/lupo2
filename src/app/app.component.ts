import {Component, OnDestroy, OnInit} from "@angular/core";
import {Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
    title = 'Lupo';
    msgs: Message[] = [];

    constructor(private router: Router,
                private authService: AuthService) {
    }

    ngOnInit() {
    }

    ngOnDestroy(): void {
    }

    isAdmin(): boolean {
        return this.authService.authStatus.isAdmin;
    }

    isLoggedIn(): boolean {
        return this.authService.authStatus.isLoggedIn
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }


}
