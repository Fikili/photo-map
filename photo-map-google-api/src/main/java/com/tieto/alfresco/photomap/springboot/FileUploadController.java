package com.tieto.alfresco.photomap.springboot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.protobuf.ByteString;
import com.tieto.alfresco.photomap.springboot.pojo.Landmark;

@RestController
public class FileUploadController {

	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public Landmark fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		Landmark landmark = new Landmark();
		List<AnnotateImageRequest> requests = new ArrayList<>();
		ByteString imgBytes = ByteString.readFrom(file.getInputStream());

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			// TODO Add exception handling
			// Only first (best) match is sufficient
			EntityAnnotation annotation = responses.get(0).getLandmarkAnnotationsList().get(0);
			LocationInfo info = annotation.getLocationsList().get(0);

			landmark.setLandmark(annotation.getDescription());
			landmark.setLatitude(info.getLatLng().getLatitude());
			landmark.setLongitude(info.getLatLng().getLongitude());

			System.out.printf("landmark: %s\n", landmark);
			return landmark;
		}
	}
}