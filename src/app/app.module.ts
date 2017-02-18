import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule} from "@angular/http";
import {RouterModule, Routes} from "@angular/router";
import {AppComponent} from "./app.component";
import {SearchBoardComponent} from "./search-board/search-board.component";
import {PersonDetailComponent} from "./person-detail/person-detail.component";
import {LdapService} from "./ldap.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

const appRoutes: Routes = [
    {path: 'search', component: SearchBoardComponent},
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
        PersonDetailComponent
    ],
    imports: [
        RouterModule.forRoot(appRoutes),
        BrowserModule,
        FormsModule,
        HttpModule,
        JsonpModule,
        NgbModule.forRoot()
    ],
    providers: [LdapService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
