# About
photo-map is Tieto project for Alfresco virtual Hackathon 2018

Main entry point is ADF application with two pages:
* Upload page where you can upload photo with "famous" landmark
* Map of the world which displays pins with landmarks. Rendering is done using [Open Street Map](https://www.openstreetmap.org) and [Leaflet](https://leafletjs.com/) library

You can use Alfresco API instead of ADF because logic is done on platform side.

# Architecture
There are several components used:
* Alfresco platform 
  * Added custom content model and metadata extractor 
* ADF
  * Main entry point for uploading photos and displaying map
* Spring boot application
  * Micro-service responsible for contacting Google Cloud Vision API

For better illustration, see the image:
![Architecture image](/photo-examples/photo-map-architecture.png)

# Application startup/details
* Alfresco with photo-map-repo jar is up and running on port 8080
  * ImageMetadataExtractor is responsible for contacting Springboot app
* Springboot app is running on port 9090
  * GOOGLE_APPLICATION_CREDENTIALS has to be declared properly, otherwise Vision API will not work
* ADF is running on <host>:4200/#/photo
  * Don't forget to specify ID in photoFolder section in [environment.ts](/photo-map-ui/src/environments/environment.ts)