package com.tieto.alfresco.photomap.springboot.pojo;

public class Landmark {

	private String label;
	private double latitude;
	private double longitude;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
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
		return "Landmark [label=" + label + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
