package com.techelevator.model.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techelevator.model.model.User;

@Service
public class UserSqlDAO implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    public UserSqlDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserByID(int userId) {
    	String getUser = "SELECT * FROM skater WHERE skater_id = ?";
    	SqlRowSet results = jdbcTemplate.queryForRowSet(getUser, userId);
    	if (results.next()) {
    		return mapRowToUser(results);
    	} else {
    		return null;
    	}
    	
    }
    
    @Override
    public int findIdByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT skater_id FROM skater WHERE skater_username = ?", int.class, username);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from skater";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        for (User user : this.findAll()) {
            if( user.getUsername().toLowerCase().equals(username.toLowerCase())) {
                return user;
            }
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {
        boolean userCreated = false;
        boolean accountCreated = false;

        // create user
        String insertUser = "insert into skater (skater_username, password_hash) values(?, ?)";
        String password_hash = new BCryptPasswordEncoder().encode(password);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String id_column = "skater_id";
        userCreated = jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(insertUser, new String[]{id_column});
                    ps.setString(1, username);
                    ps.setString(2,password_hash);
                    return ps;
                }
                , keyHolder) == 1;
        int newUserId = (int) keyHolder.getKeys().get(id_column);

        return userCreated;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("skater_id"));
        user.setUsername(rs.getString("skater_username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("ROLE_USER");
        return user;
    }
}
