package com.techelevator.model.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.model.dao.visitDAO;
import com.techelevator.model.model.visit;

@RestController
public class visitController {

	private visitDAO visitDAO;
	
	public visitController(visitDAO visitDAO) {
		this.visitDAO = visitDAO;
	}
	
	@RequestMapping(path = "/visit/{parkId}", method = RequestMethod.GET)
	public List<visit> getAllParkVisits(@PathVariable int parkId){
		logAPICall("GET- /visit/" + parkId);
		return visitDAO.getAllParkVisits(parkId);
	}
	
	@RequestMapping(path = "/visit", method = RequestMethod.POST)
	public void addVisit(@RequestBody visit aVisit) {
		logAPICall("POST- /visit" + aVisit);
		visitDAO.addVisit(aVisit);
	}
	
	public void logAPICall(String message) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
		String timeNow = now.format(formatter);
		System.out.println(timeNow + ": " + message);
	}
}
