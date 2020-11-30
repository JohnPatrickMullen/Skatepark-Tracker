package com.techelevator.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.model.visit;

@Component
public class jdbcVisitDAO implements visitDAO {

	private JdbcTemplate jdbcTemplate;
	
	public jdbcVisitDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<visit> getAllParkVisits(int parkId){
		List<visit> allParkVisits = new ArrayList<>();
		String sqlGetAllParkVisits = "SELECT * FROM visit WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParkVisits, parkId);
		
		while(results.next()) {
			visit visitResult = mapRowToVisit(results);
			allParkVisits.add(visitResult);
		}
		return allParkVisits;
	}

	@Override
	public void addVisit(visit aVisit) {
		String sqlAddVisit = "INSERT INTO visit (park_id, skater_id, visit_date) VALUES (?, ?, ?)";
		jdbcTemplate.update(sqlAddVisit, aVisit.getParkId(), aVisit.getSkaterId(), aVisit.getVisitDate());
	}

	private visit mapRowToVisit(SqlRowSet results) {
		visit aVisit = new visit();
		aVisit.setVisitId(results.getInt("visit_id"));
		aVisit.setParkId(results.getInt("park_id"));
		aVisit.setSkaterId(results.getInt("skater_id"));
		aVisit.setVisitDate(results.getString("visit_date"));
		return aVisit;
}

}