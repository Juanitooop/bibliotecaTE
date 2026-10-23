export interface Autor { id: number; nombre: string; apellido: string; nacionalidad: string; fechaNacimiento: string; }
export interface Libro { id: number; titulo: string; isbn: string; anio: number; autor: Autor; }
export interface Usuario { id: number; nombre: string; username: string; email: string; rol: 'ADMIN' | 'LECTOR'; estado: 'ACTIVO' | 'INACTIVO'; }
export interface Prestamo { id: number; libro: Libro; usuario: Usuario; fechaPrestamo: string; fechaDevolucion: string; devuelto: boolean; }
export interface Pagina<T> { content: T[]; number: number; totalPages: number; totalElements: number; }
