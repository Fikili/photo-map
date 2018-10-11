package com.tieto.alfresco.photomap;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;

import org.alfresco.repo.content.metadata.AbstractMappingMetadataExtracter;
import org.alfresco.service.cmr.repository.ContentReader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.alfresco.repo.content.MimetypeMap;

public class ImageMetadataExtractor extends AbstractMappingMetadataExtracter  {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageMetadataExtractor.class);
	
	private static final String KEY_LABEL = "label";  
	private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";  

    public static String[] SUPPORTED_MIMETYPES = new String[] {
            MimetypeMap.MIMETYPE_IMAGE_JPEG,
            MimetypeMap.MIMETYPE_IMAGE_PNG, 
            MimetypeMap.MIMETYPE_IMAGE_GIF
    };
 
    public ImageMetadataExtractor(){
        super(new HashSet<String>(Arrays.asList(SUPPORTED_MIMETYPES)));
        LOGGER.debug("");
    }
    
	@Override
	protected Map<String, Serializable> extractRaw(ContentReader reader) throws Throwable {
		
		Map<String, Serializable> rawProperties = newRawMap();
		
		InputStream in = null;
		
		try {
			
//			in = reader.getContentInputStream();  
//			String myString = IOUtils.toString(in, "UTF-8");
			
			String label = "random place";
			double latitude = getRandomLatitude();
			double longitude = getRandomLongitude();
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
		
		/*
		
        InputStream in= reader.getContentInputStream();      
        String myString = IOUtils.toString(in, "UTF-8");
        putRawValue(KEY_CUSTOM, myString, rawProperties);
        in.close();
        */

        return rawProperties;
	}
	
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
	
}
