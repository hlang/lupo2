import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PageNotFoundComponent} from "../page-not-found/page-not-found.component";
import {PersonDetailComponent} from "../person-detail/person-detail.component";
import {PersonCreateComponent} from "../person-create/person-create.component";
import {SearchBoardComponent} from "../search-board/search-board.component";

const appRoutes: Routes = [
    {path: 'search', component: SearchBoardComponent},
    {path: 'add', component: PersonCreateComponent},
    {path: 'detail/:dn', component: PersonDetailComponent},
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
