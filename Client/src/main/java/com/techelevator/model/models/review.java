package com.techelevator.model.models;

public class review {
	private int reviewId;
	private String review;
	private int score;
	private int parkId;
	private int skaterId;
	private String visitDate;
	
	
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
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
	public void setVisitDate(String now) {
		this.visitDate = now;
	}
	
}
