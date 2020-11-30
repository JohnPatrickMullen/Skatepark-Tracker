package com.techelevator.model.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.techelevator.model.dao.reviewDAO;
import com.techelevator.model.model.review;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class reviewController {
	
	private reviewDAO reviewDAO;
	
	public reviewController(reviewDAO reviewDAO) {
		this.reviewDAO = reviewDAO;
	}
	
	@RequestMapping(path = "/review/{parkId}", method = RequestMethod.GET)
	public List<review> getAllParkReviews(@PathVariable int parkId){
		logAPICall("GET-/review/" + parkId);
		return reviewDAO.getAllParkReviews(parkId);
	}
	
	@RequestMapping(path = "/review", method = RequestMethod.POST)
	public void addReview(@RequestBody review aReview) {
		logAPICall("POST- /review " + aReview);
		reviewDAO.addReview(aReview);
	}
	
	public void logAPICall(String message) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
		String timeNow = now.format(formatter);
		System.out.println(timeNow + ": " + message);
	}

}
