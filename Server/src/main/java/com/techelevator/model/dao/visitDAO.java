package com.techelevator.model.dao;

import java.util.List;

import com.techelevator.model.model.visit;

public interface visitDAO {

	List<visit> getAllParkVisits(int parkId);

	void addVisit(visit aVisit);

}
