import { Component, AfterViewInit } from '@angular/core';

const marker = L.icon({
    iconUrl: 'assets/leaflet/images/marker-icon.png',
    shadowUrl: 'assets/leaflet/images/marker-shadow.png',

});
declare let L;

@Component({
  selector: 'aca-photo-map',
  templateUrl: './photo-map.component.html',
  styleUrls: ['./photo-map.component.scss']
})
export class PhotoMapComponent implements AfterViewInit {
    
    constructor() { }
    
    
    ngAfterViewInit(): void {
        const map = L.map('mapId').setView([0, 0], 1);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        }).addTo(map);

        L.marker([51.5, -0.09], { icon: marker }).addTo(map);
    }
  

}
