import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PageNotFoundComponent} from "../page-not-found/page-not-found.component";
import {PersonDetailComponent} from "../person-detail/person-detail.component";
import {PersonCreateComponent} from "../person-create/person-create.component";
import {SearchBoardComponent} from "../search-board/search-board.component";
import {AuthGuardService} from "../auth-guard.service";
import {LoginComponent} from "../login/login.component";
import {AdminUserGuardService} from "../admin-user-guard.service";

const appRoutes: Routes = [
    {
        path: 'search',
        component: SearchBoardComponent,
        canActivate: [AuthGuardService]
    },
    {
        path: 'add',
        component: PersonCreateComponent
    },
    {
        path: 'detail/:dn',
        component: PersonDetailComponent,
        canActivate: [AdminUserGuardService]
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: '',
        redirectTo: '/search',
        pathMatch: 'full'
    },
    {path: '**', component: PageNotFoundComponent}
];

@NgModule({
    imports: [
        RouterModule.forRoot(appRoutes)
    ],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule {
}
