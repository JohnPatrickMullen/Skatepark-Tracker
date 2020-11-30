package com.techelevator.model.dao;

import java.util.List;

import com.techelevator.model.model.review;

public interface reviewDAO {

	List<review> getAllParkReviews(int parkId);

	void addReview(review aReview);

}
