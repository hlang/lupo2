import {Component} from "@angular/core";
import {NotificationService} from "./notification.service";
import {Subscription} from "rxjs/Subscription";
import {Message} from "primeng/primeng";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'Lupo2';
    msgs: Message[] = [];
    subscription: Subscription;

    constructor(private notificationService: NotificationService) {
    }

    ngOnInit() {
        this.subscribeToNotifications();
    }

    subscribeToNotifications() {
        this.subscription = this.notificationService.notificationChange
            .subscribe(notification => {
                this.msgs.length = 0;
                this.msgs.push(notification);
            });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
