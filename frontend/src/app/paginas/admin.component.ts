import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Autor, Libro, Usuario } from '../modelos';
import { ApiService, mensajeError } from '../servicios/api.service';
import { PaginadorComponent } from './paginador.component';
import { fechaLocal } from '../fecha';

type Seccion = 'autores' | 'libros' | 'usuarios';

@Component({
  standalone: true, imports: [CommonModule, FormsModule, PaginadorComponent],
  templateUrl: './admin.component.html'
})
export class AdminComponent implements OnInit {
  seccion: Seccion = 'autores';
  autores: Autor[] = []; libros: Libro[] = []; usuarios: Usuario[] = []; opcionesAutores: Autor[] = [];
  q = ''; pagina = 0; paginas = 0; total = 0; error = ''; exito = ''; ocupado = false;
  editando = false; id: number | null = null; hoy = fechaLocal();
  autor = { nombre: '', apellido: '', nacionalidad: '', fechaNacimiento: '' };
  libro = { titulo: '', isbn: '', anio: new Date().getFullYear(), autorId: 0 };
  usuario = { nombre: '', username: '', email: '', password: '', rol: 'LECTOR', estado: 'ACTIVO' };

  constructor(private api: ApiService) {}
  ngOnInit(): void { void this.cargar(0); }

  cambiar(seccion: Seccion): void {
    this.seccion = seccion; this.q = ''; this.editando = false; this.exito = '';
    void this.cargar(0);
  }

  async cargar(pagina: number): Promise<void> {
    this.ocupado = true; this.error = '';
    try {
      // Cada pestana usa su propio endpoint del servidor.
      let datos;
      if (this.seccion === 'autores') {
        datos = await this.api.listar<Autor>('autores', this.q, pagina);
        this.autores = datos.content;
      } else if (this.seccion === 'libros') {
        datos = await this.api.listar<Libro>('libros', this.q, pagina);
        this.libros = datos.content;
      } else {
        datos = await this.api.listar<Usuario>('usuarios', this.q, pagina);
        this.usuarios = datos.content;
      }
      this.pagina = datos.number; this.paginas = datos.totalPages; this.total = datos.totalElements;
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }

  async formulario(id: number | null = null): Promise<void> {
    this.error = ''; this.exito = ''; this.ocupado = true; this.id = id;
    try {
      if (this.seccion === 'autores') {
        this.autor = { nombre: '', apellido: '', nacionalidad: '', fechaNacimiento: '' };
        if (id !== null) {
          this.autor = await this.api.buscar<Autor>('autores', id);
        }
      }
      if (this.seccion === 'libros') {
        this.opcionesAutores = await this.api.opciones<Autor>('autores');
        this.libro = { titulo: '', isbn: '', anio: new Date().getFullYear(), autorId: 0 };
        if (id) {
          const libro = await this.api.buscar<Libro>('libros', id);
          this.libro = { titulo: libro.titulo, isbn: libro.isbn, anio: libro.anio, autorId: libro.autor.id };
        }
      }
      if (this.seccion === 'usuarios') {
        this.usuario = { nombre: '', username: '', email: '', password: '', rol: 'LECTOR', estado: 'ACTIVO' };
        if (id) {
          const usuario = await this.api.buscar<Usuario>('usuarios', id);
          this.usuario = { nombre: usuario.nombre, username: usuario.username, email: usuario.email, password: '', rol: usuario.rol, estado: usuario.estado };
        }
      }
      this.editando = true;
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }

  async guardar(): Promise<void> {
    this.error = ''; this.ocupado = true;
    try {
      let datos: unknown = this.autor;
      if (this.seccion === 'libros') datos = this.libro;
      if (this.seccion === 'usuarios') datos = this.usuario;
      await this.api.guardar(this.seccion, this.id, datos);
      this.editando = false; this.exito = 'Registro guardado correctamente.';
      await this.cargar(this.pagina);
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }

  async eliminar(id: number): Promise<void> {
    if (!confirm(`¿Eliminar el registro #${id}?`)) return;
    this.ocupado = true; this.error = '';
    try {
      await this.api.eliminar(this.seccion, id);
      this.editando = false; this.exito = 'Registro eliminado.';
      // Volvemos una pagina si acabamos de borrar el ultimo registro de ella.
      const cantidad = this.seccion === 'autores' ? this.autores.length : this.seccion === 'libros' ? this.libros.length : this.usuarios.length;
      await this.cargar(cantidad === 1 ? Math.max(0, this.pagina - 1) : this.pagina);
    } catch (error) { this.error = mensajeError(error); }
    finally { this.ocupado = false; }
  }
}
