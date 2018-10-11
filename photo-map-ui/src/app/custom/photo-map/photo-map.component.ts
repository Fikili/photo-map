import { Component, AfterViewInit } from '@angular/core';
import { NodesApiService, FileModel, UploadService } from '@alfresco/adf-core';
import { environment } from '../../../environments/environment';


delete L.Icon.Default.prototype._getIconUrl

L.Icon.Default.mergeOptions({
    iconRetinaUrl: "assets/leaflet/images/marker-icon-2x.png",
    iconUrl: "assets/leaflet/images/marker-icon.png",
    shadowUrl: "assets/leaflet/images/marker-shadow.png",
})

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
                console.log(node.entry.properties["photo:latitude"] + " "  + node.entry.properties["photo:longitude"]);
                L.marker([node.entry.properties["photo:latitude"], node.entry.properties["photo:longitude"]]).addTo(map);

            }
        })

    }

    submitForm(){
        let model: FileModel = new FileModel(this.selectedFile, {
            parentId: environment.photoFolder,
        });
        this.uploadService.addToQueue(model);
        this.uploadService.uploadFilesInTheQueue();
    }
  

}
