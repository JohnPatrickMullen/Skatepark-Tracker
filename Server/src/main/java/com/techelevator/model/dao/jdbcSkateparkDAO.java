package com.techelevator.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.model.skatepark;

@Component
public class jdbcSkateparkDAO implements skateparkDAO{

	private JdbcTemplate jdbcTemplate;
	
	public jdbcSkateparkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//Get all skateparks
	//Future functionality will likely include getting parks by city/state,
		// but for personal use I'll only need a small list of parks
	@Override
	public List<skatepark> getAllSkateparks(){ 
		List<skatepark> allSkateparks = new ArrayList<>();
		String sqlGetAllSkateparks = "SELECT * FROM park";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllSkateparks);
		
		while(results.next()) {
			skatepark aSkatepark = mapRowToSkatepark(results);
			allSkateparks.add(aSkatepark);
		}
		return allSkateparks;
	}
	
	
	//Get skateparks by skateparkID
	@Override
	public skatepark getSkateparkById(int parkId) {
		skatepark aSkatepark = new skatepark();
		String sqlGetPark = "Select * FROM park WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetPark, parkId);
		
		while(results.next()) {
			aSkatepark = mapRowToSkatepark(results);
		}
		
		return aSkatepark;
	}
	
	//Create a skatepark and add it to database
	@Override
	public void addNewSkatepark(skatepark aSkatepark) {
		String sqlAddSkatepark = "INSERT INTO park (park_name, park_state, park_city, indoor_outdoor, pads, notes)"
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlAddSkatepark, aSkatepark.getParkName(), aSkatepark.getState(), aSkatepark.getCity(), 
				aSkatepark.getIndoorOutdoor(), aSkatepark.getPads(), aSkatepark.getNotes());
	}
	
	//Update skatepark notes and pads based on skateparkID
	@Override
	public void updateSkatepark(skatepark aSkatepark) {
		String sqlUpdateSkatepark = "UPDATE park SET notes = ? WHERE park_id = ?";
		jdbcTemplate.update(sqlUpdateSkatepark, aSkatepark.getNotes(), aSkatepark.getParkId());
	}
	
	
	//Delete skatepark 
	
	private skatepark mapRowToSkatepark(SqlRowSet results) {
		skatepark aPark = new skatepark();
		aPark.setParkId(results.getInt("park_id"));
		aPark.setParkName(results.getString("park_name"));
		aPark.setState(results.getString("park_state"));
		aPark.setCity(results.getString("park_city"));
		aPark.setIndoorOutdoor(results.getString("indoor_outdoor"));
		aPark.setPads(results.getString("pads"));
		aPark.setNotes(results.getString("notes"));
		return aPark;
	}
}
