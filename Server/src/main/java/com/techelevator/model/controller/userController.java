package com.techelevator.model.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.model.dao.UserDAO;
import com.techelevator.model.model.User;

@RestController
public class userController {
	
	private UserDAO userDAO;
	
	public userController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@RequestMapping(path = "/skater/{skaterId}", method = RequestMethod.GET)
	public User getUserByID(@PathVariable int skaterId) {
		logAPICall("GET- /skater/" + skaterId);
		return userDAO.getUserByID(skaterId);
	}
	
	public void logAPICall(String message) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
		String timeNow = now.format(formatter);
		System.out.println(timeNow + ": " + message);
	}

}
