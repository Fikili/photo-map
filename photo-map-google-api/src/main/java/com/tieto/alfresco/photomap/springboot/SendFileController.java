package com.tieto.alfresco.photomap.springboot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class SendFileController {

	@RequestMapping(value = "/upload", method = RequestMethod.POST)

	public Landmark fileUpload(@RequestBody String body) throws IOException {

		Landmark landmark = new Landmark();
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteString imgBytes = ByteString.copyFrom(Base64.getDecoder().decode(body));

		// Google code starts
		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();
			
			if (responses.get(0).getLandmarkAnnotationsList().isEmpty()) {
				return null;
			}
			
			// TODO Add exception handling
			// Only first (best) match is sufficient
			EntityAnnotation annotation = responses.get(0).getLandmarkAnnotationsList().get(0);
			LocationInfo info = annotation.getLocationsList().get(0);

			landmark.setLabel(annotation.getDescription());
			landmark.setLatitude(info.getLatLng().getLatitude());
			landmark.setLongitude(info.getLatLng().getLongitude());

			System.out.printf("landmark: %s\n", landmark);
			return landmark;
		}
	}
}