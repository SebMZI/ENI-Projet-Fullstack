import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-custom-multiple-select',
  imports: [],
  templateUrl: './custom-multiple-select.html',
  styleUrl: './custom-multiple-select.css',
})
export class CustomMultipleSelect {
  dropdownoepn = false;
  @Input() label: string = '';
  @Input() items: string[] = [];

  @Output() selectedItemChange = new EventEmitter<string>();

  public toggleDropdown() {
    this.dropdownoepn = !this.dropdownoepn;
  }

  public selectItem(item: string) {
    this.selectedItemChange.emit(item);
  }

  public getLabel() {
    return this.label;
  }

  public getItems() {
    return this.items;
  }
}
