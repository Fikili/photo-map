/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tieto.alfresco.google.vision;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// [START vision_quickstart]
// Imports the Google Cloud client library

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

/**
 * Example provided by Google
 * It is not used in spring boot 
 */
public class QuickstartSample {
	public static void main(String... args) throws Exception {
		//detectLandmarks("./src/main/resources/1.jpg", System.out);
	}

	public static void detectLandmarks(String filePath, PrintStream out) throws Exception, IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Type.LANDMARK_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					out.printf("Error: %s\n", res.getError().getMessage());
					return;
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
				for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
					LocationInfo info = annotation.getLocationsList().listIterator().next();
					out.printf("Landmark: %s\n %s\n", annotation.getDescription(), info.getLatLng());
				}
			}
		}
	}

}
// [END vision_quickstart]
