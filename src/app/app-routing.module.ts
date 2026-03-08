import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { UserInfoComponent } from './user-info/user-info.component';
import { logginGuardGuard } from './guards/loggin-guard.guard';
import { loginReverseGuard } from './guards/login-reverse.guard';

const routes: Routes = [
  {path:'login', component: LoginPageComponent, canActivate : [loginReverseGuard]},
  {path:'user', component: UserInfoComponent},
  {path:'dashboard', loadChildren: ()=> import("./dashboard/dashboard.module").then(m => m.DashboardModule),canActivate : [logginGuardGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
