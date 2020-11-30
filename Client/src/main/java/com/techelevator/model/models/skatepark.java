package com.techelevator.model.models;

public class skatepark {
	private int parkId;
	private	String parkName;
	private String state;
	private String city;
	private String indoorOutdoor;
	private String pads;
	private String notes;
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIndoorOutdoor() {
		return indoorOutdoor;
	}
	public void setIndoorOutdoor(String indoorOutdoor) {
		this.indoorOutdoor = indoorOutdoor;
	}
	public String getPads() {
		return pads;
	}
	public void setPads(String pads) {
		this.pads = pads;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
