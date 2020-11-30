package com.techelevator.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.model.review;

@Component
public class jdbcReviewDAO implements reviewDAO{

	private JdbcTemplate jdbcTemplate;
	
	public jdbcReviewDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<review> getAllParkReviews(int parkId){
		List<review> allParkReviews = new ArrayList<>();
		String sqlGetAllParkReviews = "SELECT * FROM review WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParkReviews, parkId);
		
		while(results.next()) {
			review reviewResult = mapRowToReview(results);
			allParkReviews.add(reviewResult);
		}
		return allParkReviews;
	} 
	
	@Override
	public void addReview(review aReview) {
		String sqlAddReview = "INSERT INTO review (review, review_score, park_id, skater_id, visit_date) "
				+ "VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sqlAddReview, aReview.getReview(), aReview.getScore(), aReview.getParkId(), aReview.getSkaterId(), aReview.getVisitDate());
	}
	
	private review mapRowToReview(SqlRowSet results) {
		review aReview = new review();
		aReview.setReviewId(results.getInt("review_id"));
		aReview.setReview(results.getString("review"));
		aReview.setScore(results.getInt("review_score"));
		aReview.setParkId(results.getInt("park_id"));
		aReview.setSkaterId(results.getInt("skater_id"));
		aReview.setVisitDate(results.getString("visit_Date"));
		return aReview;
	}
}
