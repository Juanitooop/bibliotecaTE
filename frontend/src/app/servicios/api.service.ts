import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { Pagina } from '../modelos';

// Angular envia /api al servidor usando proxy.conf.json.
export const API_URL = '/api';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  // T indica el tipo de dato: libro, autor, usuario o prestamo.
  // firstValueFrom permite esperar la respuesta con await.
  listar<T>(recurso: string, q = '', page = 0, size = 5): Promise<Pagina<T>> {
    return firstValueFrom(this.http.get<Pagina<T>>(`${API_URL}/${recurso}`, { params: { q, page, size } }));
  }

  buscar<T>(recurso: string, id: number): Promise<T> {
    return firstValueFrom(this.http.get<T>(`${API_URL}/${recurso}/${id}`));
  }

  guardar<T>(recurso: string, id: number | null, datos: unknown): Promise<T> {
    // Si hay id, editamos. Si no hay id, creamos un registro.
    if (id !== null) return firstValueFrom(this.http.put<T>(`${API_URL}/${recurso}/${id}`, datos));
    return firstValueFrom(this.http.post<T>(`${API_URL}/${recurso}`, datos));
  }

  eliminar(recurso: string, id: number): Promise<void> {
    return firstValueFrom(this.http.delete<void>(`${API_URL}/${recurso}/${id}`));
  }

  async opciones<T>(recurso: string): Promise<T[]> {
    // Leemos todas las paginas para que no falten opciones en los formularios.
    const resultado: T[] = [];
    let page = 0;
    let paginas = 1;
    while (page < paginas) {
      const datos = await this.listar<T>(recurso, '', page, 100);
      for (const registro of datos.content) {
        resultado.push(registro);
      }
      paginas = datos.totalPages;
      page++;
    }
    return resultado;
  }
}

export function mensajeError(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    if (error.status === 0) return 'No se pudo conectar con Spring Boot. Revisa que este encendido en el puerto 8081.';
    const campos = error.error?.campos;
    if (campos) return Object.entries(campos).map(([campo, mensaje]) => `${campo}: ${mensaje}`).join('. ');
    return error.error?.mensaje || 'No se pudo completar la operacion.';
  }
  return 'Ocurrio un error inesperado.';
}
