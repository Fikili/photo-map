package com.tieto.alfresco.photomap.springboot.pojo;

public class Landmark {

	private String landmark;
	private double latitude;
	private double longitude;
	
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Landmark [name=" + landmark + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
