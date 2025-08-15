import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter',
  standalone: false
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], searchText: string): any[] {
    if (!items) return [];
    if (!searchText) return items;

    searchText = searchText.toLowerCase();

    return items.filter(item => {
      // Recherche dans toutes les propriétés de l'objet
      return Object.keys(item).some(key => {
        // Ignorer les propriétés de type objet ou tableau
        if (typeof item[key] === 'object' || Array.isArray(item[key])) {
          return false;
        }
        
        // Convertir en chaîne de caractères et rechercher
        const value = item[key]?.toString().toLowerCase();
        return value?.includes(searchText);
      });
    });
  }
}

