import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-paginador', standalone: true,
  template: `<div class="paginador"><span>{{ total }} registros · Página {{ paginas === 0 ? 0 : pagina + 1 }} de {{ paginas }}</span><div><button class="secundario" [disabled]="ocupado || pagina === 0" (click)="cambio.emit(pagina - 1)">Anterior</button><button class="secundario" [disabled]="ocupado || pagina + 1 >= paginas" (click)="cambio.emit(pagina + 1)">Siguiente</button></div></div>`
})
export class PaginadorComponent {
  @Input() pagina = 0;
  @Input() paginas = 0;
  @Input() total = 0;
  @Input() ocupado = false;
  @Output() cambio = new EventEmitter<number>();
}
