import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root', standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterOutlet],
  template: `<header><a class="marca" routerLink="/libros">Biblioteca</a>
    <nav><a routerLink="/libros" routerLinkActive="activo">Libros</a>
    <a routerLink="/prestamos" routerLinkActive="activo">Préstamos</a>
    <a routerLink="/admin" routerLinkActive="activo">Administración</a></nav>
    </header><main><router-outlet /></main>`
})
export class AppComponent {}
