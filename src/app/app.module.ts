import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {SearchBoardComponent} from "./search-board/search-board.component";
import {LdapService} from "./ldap.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
    declarations: [
        AppComponent,
        SearchBoardComponent
    ],
    imports: [
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
