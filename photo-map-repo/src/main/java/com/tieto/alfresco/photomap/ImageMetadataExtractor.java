package com.tieto.alfresco.photomap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;

import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.metadata.AbstractMappingMetadataExtracter;
import org.alfresco.service.cmr.repository.ContentReader;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageMetadataExtractor extends AbstractMappingMetadataExtracter  {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageMetadataExtractor.class);
	
	private static final String KEY_LABEL = "label";  
	private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";  
    
    // TODO: Configure Spring Boot app URL
    private static final String SPRING_BOOT_APP_URL = "http://localhost:9090/upload";

    public static String[] SUPPORTED_MIMETYPES = new String[] {
            MimetypeMap.MIMETYPE_IMAGE_JPEG,
            MimetypeMap.MIMETYPE_IMAGE_PNG, 
            MimetypeMap.MIMETYPE_IMAGE_GIF
    };
 
    public ImageMetadataExtractor(){
        super(new HashSet<String>(Arrays.asList(SUPPORTED_MIMETYPES)));
    }
    
	@Override
	protected Map<String, Serializable> extractRaw(ContentReader reader) throws Throwable {
		
		Map<String, Serializable> rawProperties = newRawMap();
		InputStream in = reader.getContentInputStream();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(SPRING_BOOT_APP_URL);
		post.setHeader("Content-Type", "text/plain");
		
		String base64Image = Base64.getEncoder().encodeToString(IOUtils.toByteArray(in));
		post.setEntity(new StringEntity(base64Image));
		
		HttpResponse response = client.execute(post);
		LOGGER.debug("Response Code : {}", response.getStatusLine().getStatusCode());
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer output = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			output.append(line);
		}
		
		LOGGER.debug("output: {}", output);
		
		if (output.length() == 0) {
			return null;
		}
		
		JSONObject jsonObject = new JSONObject(output.toString());
		LOGGER.debug("output: {}", jsonObject);
		
		try {
			
			
			String label = jsonObject.getString(KEY_LABEL);
			double latitude = jsonObject.getDouble(KEY_LATITUDE);
			double longitude = jsonObject.getDouble(KEY_LONGITUDE);
			//double latitude = getRandomLatitude();
			//double longitude = getRandomLongitude();
			LOGGER.debug("putting raw values: label {}, latitude {}, longitude {}", label, latitude, longitude);
			
			putRawValue(KEY_LABEL, label, rawProperties);
	        putRawValue(KEY_LATITUDE, latitude, rawProperties);
	        putRawValue(KEY_LONGITUDE, longitude, rawProperties);
	        
		} catch (Exception ex) {
			LOGGER.debug("Exception " + ex.toString());
		} finally {
			if(in != null) {
				in.close();
			}
			in = null;
		}

        return rawProperties;
	}
	
	/*
	// Latitude -90 to +90
	private double getRandomLatitude() {
		return getRandomFromRange(-90, 90);
	}

	// Longitude 180 to +180
	private double getRandomLongitude() {
		return getRandomFromRange(-180, 180);
	}

	private double getRandomFromRange(double min, double max) {
		return min + (double)(Math.random() * ((max - min) + 1));
	}
	*/
}
