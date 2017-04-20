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
import {AuthGuardService} from "./auth-guard.service";
import {AuthService} from "./auth.service";
import {LoginComponent} from "./login/login.component";
import {AdminGuardService} from "./admin-guard.service";
import {AdminUserGuardService} from "./admin-user-guard.service";


@NgModule({
    declarations: [
        AppComponent,
        SearchBoardComponent,
        PersonDetailComponent,
        PersonCreateComponent,
        EqualValidator,
        PageNotFoundComponent,
        LoginComponent
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
        NotificationService,
        AuthService,
        AuthGuardService,
        AdminGuardService,
        AdminUserGuardService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
