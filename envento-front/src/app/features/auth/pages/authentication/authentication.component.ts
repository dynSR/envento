import {Component} from '@angular/core';
import {RegisterFormComponent} from '../../components/register-form/register-form.component';
import {LoginFormComponent} from '../../components/login-form/login-form.component';

@Component({
  selector: 'app-authentication',
  standalone: true,
  imports: [LoginFormComponent, RegisterFormComponent],
  templateUrl: './authentication.component.html',
  styleUrl: './authentication.component.css'
})
export class AuthenticationComponent {

}
