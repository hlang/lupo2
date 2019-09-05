import {Component, OnInit} from "@angular/core";
import {InfoService} from "../info.service";

@Component({
    selector: 'app-app-footer',
    templateUrl: './app-footer.component.html',
    styleUrls: ['./app-footer.component.css']
})
export class AppFooterComponent implements OnInit {

    version: string = '';

    constructor(private infoService: InfoService) {
    }

    ngOnInit() {
        this.infoService.getInfoVersion()
            .subscribe(
                version => this.version = version
            );
    }

}
