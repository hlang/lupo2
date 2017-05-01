import {Component, OnDestroy, OnInit} from "@angular/core";
import {NotificationService} from "./notification.service";
import {Subscription} from "rxjs/Subscription";
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
    subscription: Subscription;

    constructor(private router: Router,
                private authService: AuthService,
                private notificationService: NotificationService) {
    }

    ngOnInit() {
        this.subscribeToNotifications();
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

    subscribeToNotifications() {
        this.subscription = this.notificationService.notificationChange
            .subscribe(notification => {
                this.msgs.push(notification);
            });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
