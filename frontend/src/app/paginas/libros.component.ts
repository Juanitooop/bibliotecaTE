import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Libro } from '../modelos';
import { ApiService, mensajeError } from '../servicios/api.service';
import { PaginadorComponent } from './paginador.component';

@Component({
  standalone: true, imports: [CommonModule, FormsModule, RouterLink, PaginadorComponent],
  template: `<h1>Libros</h1>
    <form class="busqueda" (ngSubmit)="cargar(0)"><label>Buscar por título o autor<input name="q" [(ngModel)]="q" placeholder="Ejemplo: Gabriel"></label><button [disabled]="ocupado">Buscar</button></form>
    <p class="error" role="alert" *ngIf="error">{{ error }}</p><p *ngIf="ocupado">Cargando libros...</p>
    <div class="tabla"><table><thead><tr><th>Título</th><th>Autor</th><th>Año</th><th>Acciones</th></tr></thead><tbody><tr *ngFor="let libro of libros"><td>{{ libro.titulo }}</td><td>{{ libro.autor.nombre }} {{ libro.autor.apellido }}</td><td>{{ libro.anio }}</td><td><a [routerLink]="['/libros', libro.id]">Ver detalle</a></td></tr></tbody></table></div>
    <p class="vacio" *ngIf="!ocupado && !error && libros.length === 0">No se encontraron libros. Prueba otra búsqueda o agrega libros desde Administración.</p>
    <app-paginador [pagina]="pagina" [paginas]="paginas" [total]="total" [ocupado]="ocupado" (cambio)="cargar($event)" />`
})
export class LibrosComponent implements OnInit {
  libros: Libro[] = []; q = ''; pagina = 0; paginas = 0; total = 0; error = ''; ocupado = false;
  constructor(private api: ApiService) {}
  ngOnInit(): void { void this.cargar(0); }
  async cargar(pagina: number): Promise<void> {
    this.ocupado = true; this.error = '';
    try {
      const datos = await this.api.listar<Libro>('libros', this.q, pagina);
      this.libros = datos.content; this.pagina = datos.number; this.paginas = datos.totalPages; this.total = datos.totalElements;
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }
}
