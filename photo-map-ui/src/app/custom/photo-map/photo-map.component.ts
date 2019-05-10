import { Component, AfterViewInit } from '@angular/core';
import { NodesApiService, FileModel, UploadService } from '@alfresco/adf-core';
import { environment } from '../../../environments/environment';
import * as Leaflet from "leaflet";
import 'leaflet-defaulticon-compatibility';
import * as cl from 'leaflet.markercluster';


@Component({
    selector: 'aca-photo-map',
    templateUrl: './photo-map.component.html',
    styleUrls: ['./photo-map.component.scss']
})
export class PhotoMapComponent implements AfterViewInit {
    selectedFile: File;
    map: Leaflet.Map;
    layerGroup: cl.MarkerClusterGroup;

    constructor(public nodesApiService: NodesApiService,
        private uploadService: UploadService) { }

    onFileChange($event) {
        this.selectedFile = $event.currentTarget.files[0];
    }

    ngAfterViewInit(): void {

        this.redrawMap(true);
    }

    redrawMap(init:boolean): void {
        this.nodesApiService.getNodeChildren(environment.photoFolder, {"include":["aspectNames","properties"]}).subscribe(nodes => {
            if (init) {
                this.map = Leaflet.map('mapId').setView([0, 0], 1);
                Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                }).addTo(this.map);
                this.layerGroup = new cl.MarkerClusterGroup().addTo(this.map);
            }
            this.layerGroup.clearLayers();
            for (let node of nodes.list.entries) {
                if (node.entry.aspectNames.includes("photo:metadata")){
                    console.log(node.entry.name);
                    console.log(node.entry.properties["photo:latitude"] + " " + node.entry.properties["photo:longitude"]);
                    this.layerGroup.addLayer(Leaflet.marker([node.entry.properties["photo:latitude"], node.entry.properties["photo:longitude"]]).addTo(this.layerGroup)
                    .bindPopup(node.entry.properties["photo:label"] + "<br>[" + node.entry.properties["photo:latitude"] + ", " + node.entry.properties["photo:longitude"] + "]"));
                    
                } 
            }

        });
    }

    submitForm() {
        let model: FileModel = new FileModel(this.selectedFile, {
            parentId: environment.photoFolder,
        });
        this.uploadService.addToQueue(model);
        this.uploadService.uploadFilesInTheQueue();
        this.uploadService.fileUploadComplete.subscribe(node =>{
            this.redrawMap(false);
        });
    }

}
