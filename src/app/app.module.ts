import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule} from "@angular/http";
import {GrowlModule} from "primeng/primeng";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppComponent} from "./app.component";
import {SearchBoardComponent} from "./search-board/search-board.component";
import {PersonDetailComponent} from "./person-detail/person-detail.component";
import {PersonCreateComponent} from "./person-create/person-create.component";
import {LdapService} from "./ldap.service";
import {EqualValidator} from "./equal-validator.directive";
import {NotificationService} from "./notification.service";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {AppRoutingModule} from "./app-routing/app-routing.module";


@NgModule({
    declarations: [
        AppComponent,
        SearchBoardComponent,
        PersonDetailComponent,
        PersonCreateComponent,
        EqualValidator,
        PageNotFoundComponent
    ],
    imports: [
        AppRoutingModule,
        BrowserModule,
        FormsModule,
        HttpModule,
        JsonpModule,
        GrowlModule,
        NgbModule.forRoot()
    ],
    providers: [
        LdapService,
        NotificationService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
