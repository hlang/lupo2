import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule} from "@angular/http";
import {RouterModule, Routes} from "@angular/router";
import {GrowlModule} from "primeng/primeng";
import {AppComponent} from "./app.component";
import {SearchBoardComponent} from "./search-board/search-board.component";
import {PersonDetailComponent} from "./person-detail/person-detail.component";
import {PersonCreateComponent} from "./person-create/person-create.component";
import {LdapService} from "./ldap.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {EqualValidator} from "./equal-validator.directive";
import {NotificationService} from "./notification.service";

const appRoutes: Routes = [
    {path: 'search', component: SearchBoardComponent},
    {path: 'add', component: PersonCreateComponent},
    {path: 'detail/:dn', component: PersonDetailComponent},
    {
        path: '',
        redirectTo: '/search',
        pathMatch: 'full'
    }
];

@NgModule({
    declarations: [
        AppComponent,
        SearchBoardComponent,
        PersonDetailComponent,
        PersonCreateComponent,
        EqualValidator
    ],
    imports: [
        RouterModule.forRoot(appRoutes),
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
