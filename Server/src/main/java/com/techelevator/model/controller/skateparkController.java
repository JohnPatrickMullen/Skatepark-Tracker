package com.techelevator.model.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.model.dao.skateparkDAO;
import com.techelevator.model.model.skatepark;

@RestController
public class skateparkController {
	
	private skateparkDAO skateparkDAO;
	
	public skateparkController(skateparkDAO skateparkDAO) {
		this.skateparkDAO = skateparkDAO;
	}
	
	@RequestMapping(path = "/park", method = RequestMethod.GET)
	public List<skatepark> getAllParks(){
		logAPICall("GET-/park/");
		return skateparkDAO.getAllSkateparks();
	}
	
	@RequestMapping(path = "/park/{parkId}", method = RequestMethod.GET)
	public skatepark getParkByParkId(@PathVariable int parkId) {
		logAPICall("GET/park/search?park_id= " + parkId);
		return skateparkDAO.getSkateparkById(parkId);
	}
	
	@RequestMapping(path = "/park", method = RequestMethod.POST)
	public void addPark(@RequestBody skatepark aSkatepark) {
		logAPICall("POST- /park " + aSkatepark);
		skateparkDAO.addNewSkatepark(aSkatepark);
	}
	
	@RequestMapping(path = "/park", method = RequestMethod.PUT)
	public void updatePark(@RequestBody skatepark aSkatepark) {
		logAPICall("PUT-/park for " + aSkatepark.getParkId() + "new note: " + aSkatepark.getNotes());
		skateparkDAO.updateSkatepark(aSkatepark);
	}
	
	public void logAPICall(String message) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss.A");
		String timeNow = now.format(formatter);
		System.out.println(timeNow + ": " + message);
	}

}
