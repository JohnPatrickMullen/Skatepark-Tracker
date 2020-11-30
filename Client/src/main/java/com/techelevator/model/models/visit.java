package com.techelevator.model.models;


public class visit {
	private int visitId;
	private int parkId;
	private int skaterId;
	private String visitDate;
	
	public int getVisitId() {
		return visitId;
	}
	public void setVisitId(int visitId) {
		this.visitId = visitId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public int getSkaterId() {
		return skaterId;
	}
	public void setSkaterId(int skaterId) {
		this.skaterId = skaterId;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
}
