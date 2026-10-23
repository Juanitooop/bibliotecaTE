import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Libro, Usuario, Prestamo } from '../modelos';
import { ApiService, mensajeError } from '../servicios/api.service';
import { PaginadorComponent } from './paginador.component';
import { fechaLocal } from '../fecha';

@Component({
  standalone: true, imports: [CommonModule, FormsModule, PaginadorComponent],
  templateUrl: './prestamos.component.html'
})
export class PrestamosComponent implements OnInit {
  prestamos: Prestamo[] = []; libros: Libro[] = []; usuarios: Usuario[] = [];
  q = ''; pagina = 0; paginas = 0; total = 0; error = ''; exito = ''; ocupado = false;
  editando = false; id: number | null = null; detalle: Prestamo | null = null;
  hoy = fechaLocal();
  datos = { libroId: 0, usuarioId: 0, fechaPrestamo: this.hoy, fechaDevolucion: '', devuelto: false };
  constructor(private api: ApiService) {}
  ngOnInit(): void { void this.cargar(0); }

  async cargar(pagina: number): Promise<void> {
    this.ocupado = true; this.error = '';
    try {
      const datos = await this.api.listar<Prestamo>('prestamos', this.q, pagina);
      this.prestamos = datos.content; this.pagina = datos.number; this.paginas = datos.totalPages; this.total = datos.totalElements;
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }

  async formulario(prestamo?: Prestamo): Promise<void> {
    this.error = ''; this.exito = ''; this.ocupado = true;
    try {
      this.libros = await this.api.opciones<Libro>('libros');
      // Solo los lectores activos aparecen para un prestamo nuevo.
      const usuarios = await this.api.opciones<Usuario>('usuarios');
      this.usuarios = [];
      let usuarioIncluido = false;
      for (const usuario of usuarios) {
        if (usuario.rol === 'LECTOR' && usuario.estado === 'ACTIVO') {
          this.usuarios.push(usuario);
          if (prestamo && usuario.id === prestamo.usuario.id) {
            usuarioIncluido = true;
          }
        }
      }
      this.id = null;
      this.datos = { libroId: 0, usuarioId: 0, fechaPrestamo: this.hoy, fechaDevolucion: '', devuelto: false };
      if (prestamo) {
        // Permite devolver un prestamo aunque el lector ahora este inactivo.
        if (!usuarioIncluido) {
          this.usuarios.push(prestamo.usuario);
        }
        this.id = prestamo.id;
        this.datos = {
          libroId: prestamo.libro.id, usuarioId: prestamo.usuario.id,
          fechaPrestamo: prestamo.fechaPrestamo, fechaDevolucion: prestamo.fechaDevolucion,
          devuelto: prestamo.devuelto
        };
      }
      this.editando = true;
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }

  async guardar(): Promise<void> {
    this.error = ''; this.ocupado = true;
    try {
      await this.api.guardar('prestamos', this.id, this.datos);
      this.editando = false; this.detalle = null; this.exito = 'Préstamo guardado.';
      await this.cargar(this.pagina);
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }

  async ver(id: number): Promise<void> {
    this.error = '';
    try { this.detalle = await this.api.buscar<Prestamo>('prestamos', id); }
    catch (error) { this.error = mensajeError(error); }
  }

  async eliminar(prestamo: Prestamo): Promise<void> {
    if (!confirm(`¿Eliminar el préstamo #${prestamo.id}?`)) return;
    this.ocupado = true; this.error = '';
    try {
      await this.api.eliminar('prestamos', prestamo.id);
      this.detalle = null; this.exito = 'Préstamo eliminado.';
      await this.cargar(this.prestamos.length === 1 ? Math.max(0, this.pagina - 1) : this.pagina);
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }
}
