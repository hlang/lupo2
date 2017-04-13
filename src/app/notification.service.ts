import {Injectable} from "@angular/core";
import {Subject} from "rxjs/Subject";
import {Message} from "primeng/primeng";

@Injectable()
export class NotificationService {

    notificationChange: Subject<Message> = new Subject<Message>();

    constructor() {
    }


    notify(msg: Message) {
        this.notificationChange.next(msg);
    }

}
