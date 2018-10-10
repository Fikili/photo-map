# About
photo-map is Tieto project for Alfresco virtual Hackathon 2018

Main entry point is ADF application with two pages:
* Upload page where you can upload photo with "famous" landmark
* Map of the world which displays pins with landmarks. Rendering is done using [Open Street Map](https://www.openstreetmap.org) and [Leaflet](https://leafletjs.com/)

You can use Alfresco API instead of ADF because logic is done on platform side.

# Architecture
There are several components used:
* Alfresco platform - Added custom content model and metadata extractor 
* ADF - Main UI
* Spring boot application - Micro-service responsible for contacting Google Cloud Vision API

For better illustration, see the image:
![Architecture image](/photo-examples/photo-map-architecture.png)
