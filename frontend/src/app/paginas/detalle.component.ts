import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Libro } from '../modelos';
import { ApiService, mensajeError } from '../servicios/api.service';

@Component({
  standalone: true, imports: [CommonModule, RouterLink],
  template: `<a routerLink="/libros">← Volver al catálogo</a><p class="error" role="alert" *ngIf="error">{{ error }}</p>
    <section *ngIf="libro" class="detalle"><div><h1>{{ libro.titulo }}</h1><p>ISBN: {{ libro.isbn }} · Año: {{ libro.anio }}</p>
    <h2>Acerca del autor</h2><dl><dt>Nombre</dt><dd>{{ libro.autor.nombre }} {{ libro.autor.apellido }}</dd><dt>Nacionalidad</dt><dd>{{ libro.autor.nacionalidad }}</dd><dt>Fecha de nacimiento</dt><dd>{{ libro.autor.fechaNacimiento | date:'dd/MM/yyyy' }}</dd></dl></div>
    <a routerLink="/prestamos">Registrar un préstamo</a></section>`
})
export class DetalleComponent implements OnInit {
  libro: Libro | null = null; error = '';
  constructor(private api: ApiService, private ruta: ActivatedRoute) {}
  async ngOnInit(): Promise<void> {
    try { this.libro = await this.api.buscar<Libro>('libros', Number(this.ruta.snapshot.paramMap.get('id'))); }
    catch (error) { this.error = mensajeError(error); }
  }
}
