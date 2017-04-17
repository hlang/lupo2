import {Component} from "@angular/core";
import {NotificationService} from "./notification.service";
import {Subscription} from "rxjs/Subscription";
import {Message} from "primeng/primeng";
import {Http} from "@angular/http";
import {Router} from "@angular/router";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'Lupo2';
    msgs: Message[] = [];
    subscription: Subscription;

    constructor(private http: Http,
                private router: Router,
                private notificationService: NotificationService) {
    }

    ngOnInit() {

    }

    logout() {
        this.http.post("logout", {})
            .subscribe();
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
