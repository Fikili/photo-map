import { Component, AfterViewInit } from '@angular/core';
import { NodesApiService, FileModel, UploadService } from '@alfresco/adf-core';
import { environment } from '../../../environments/environment';

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
    selectedFile: File;
    
    constructor(private nodesApiService: NodesApiService,
            	private uploadService: UploadService) { }
    
    onFileChange($event) {
        this.selectedFile = $event.currentTarget.files[0];
    }



    ngAfterViewInit(): void {
        const map = L.map('mapId').setView([0, 0], 1);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        }).addTo(map);

        this.nodesApiService.getNodeChildren(environment.photoFolder).subscribe(nodes =>{
            for(let node of nodes.list.entries){
                console.log(node.entry.name);
            }
        })

        L.marker([51.5, -0.09], { icon: marker }).addTo(map);
    }

    submitForm(){
        let model: FileModel = new FileModel(this.selectedFile, {
            parentId: environment.photoFolder,
        });
        this.uploadService.addToQueue(model);
        this.uploadService.uploadFilesInTheQueue();
    }
  

}
