import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {
    ConfirmationService,
    ConfirmDialogModule,
    GrowlModule,
    InputTextModule,
    MessageService,
    PaginatorModule,
    PanelModule,
    SharedModule,
    TooltipModule
} from "primeng/primeng";
import {TableModule} from "primeng/table";
import {ToastModule} from "primeng/toast";

import {AppComponent} from "./app.component";
import {SearchBoardComponent} from "./search-board/search-board.component";
import {PersonDetailComponent} from "./person-detail/person-detail.component";
import {PersonCreateComponent} from "./person-create/person-create.component";
import {LdapService} from "./ldap.service";
import {EqualValidator} from "./equal-validator.directive";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {AppRoutingModule} from "./app-routing/app-routing.module";
import {AuthGuardService} from "./auth-guard.service";
import {AuthService} from "./auth.service";
import {LoginComponent} from "./login/login.component";
import {AdminUserGuardService} from "./admin-user-guard.service";
import {InfoService} from "./info.service";
import {AppFooterComponent} from "./app-footer/app-footer.component";


@NgModule({
    declarations: [
        AppComponent,
        SearchBoardComponent,
        PersonDetailComponent,
        PersonCreateComponent,
        EqualValidator,
        PageNotFoundComponent,
        LoginComponent,
        AppFooterComponent
    ],
    imports: [
        AppRoutingModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpClientModule,
        GrowlModule,
        PanelModule,
        TooltipModule,
        TableModule,
        PaginatorModule,
        ConfirmDialogModule,
        ToastModule,
        InputTextModule,
        SharedModule
    ],
    providers: [
        LdapService,
        MessageService,
        ConfirmationService,
        AuthService,
        InfoService,
        AuthGuardService,
        AdminUserGuardService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
