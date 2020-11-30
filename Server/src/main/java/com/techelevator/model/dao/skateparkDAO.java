package com.techelevator.model.dao;

import java.util.List;

import com.techelevator.model.model.skatepark;

public interface skateparkDAO {

	List<skatepark> getAllSkateparks();

	skatepark getSkateparkById(int parkId);

	void addNewSkatepark(skatepark aSkatepark);

	void updateSkatepark(skatepark aSkatepark);

}
