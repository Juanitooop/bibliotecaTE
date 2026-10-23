import { Routes } from '@angular/router';
import { LibrosComponent } from './paginas/libros.component';
import { DetalleComponent } from './paginas/detalle.component';
import { PrestamosComponent } from './paginas/prestamos.component';
import { AdminComponent } from './paginas/admin.component';

export const routes: Routes = [
  { path: 'login', redirectTo: 'libros', pathMatch: 'full' },
  { path: 'libros', component: LibrosComponent },
  { path: 'libros/:id', component: DetalleComponent },
  { path: 'prestamos', component: PrestamosComponent },
  { path: 'admin', component: AdminComponent },
  { path: '', redirectTo: 'libros', pathMatch: 'full' },
  { path: '**', redirectTo: 'libros' }
];
